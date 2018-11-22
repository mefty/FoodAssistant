%Policy
% --------------------------------------------------------------------------
% Normally, cook something with meat.
% You can cook souvla only if it is a special occasion. (But normally
% prefer to cook meat.)
%If it is a special occasion but you don’t have money cook
%something with meat. If it’s fasting period, normally cook
% legumes. If it’s fasting period and a special occasion cook mollusks
% (But normally prefer to cook legumes). If it’s a special occasion and
% fasting period but you don’t have money , cook legumes. If you have
% allergy to meat you cook legumes or molluscs. If you have allergy to
% mollusks cook meat. If you have to both cook legumes.
% --------------------------------------------------------------------------
% Scenarios: <1, {}, cook(Day,meat)> <2, {specialOccasion(Day}, cook(Day,
% souvla)> <3, {fastingPeriod(Day)}, cook(Day, legumes)> <4,
% {fastingPeriod(Day),specialOccasion(Day)}, cook(Day, molluscs)> <5,
% {noMoney(Day)}, cook(Day,meat)> <6, {fastingPeriod(Day),noMoney (Day)},
% cook(Day, legumes)> <7,
% {allergyMeat},cook(Day,legumes);cook(Day,mollusks)> <8,
% {allergyMollusks},cook(Day, meat);cook(Day,souvla);cook(Day,legumes)>
% <9, {allergyMollusks,allergyMeat},cook(Day,legumes)>
%-------------------------------------------------------------------------
%
%add gorgias to the program
:- dynamic allergyMeat/0, allergyMolluscs/0, noMoney/0, fasting/0, specialOccasion/0.
:- compile('gorgias-src-0.6d/lib/gorgias').
:- compile('gorgias-src-0.6d/ext/lpwnf').

%Write here the known facts about your world(sensors).
%

%We make sure that we can't cook 2 things the same day.
complement(cook(Day,meat),cook(Day,souvla)).
complement(cook(Day,souvla),cook(Day,meat)).
complement(cook(Day,meat),cook(Day,legumes)).
complement(cook(Day,legumes),cook(Day,meat)).
complement(cook(Day,souvla),cook(Day,molluscs)).
complement(cook(Day,molluscs),cook(Day,souvla)).
complement(cook(Day,legumes),cook(Day,molluscs)).
complement(cook(Day,molluscs),cook(Day,legumes)).
complement(cook(Day,meat),cook(Day,molluscs)).
complement(cook(Day,molluscs),cook(Day,meat)).
complement(cook(Day,legumes),cook(Day,souvla)).
complement(cook(Day,souvla),cook(Day,legumes)).


rule(r1(Day),cook(Day,meat),[]):-not(allergyMeat). %normally cook meat.
rule(r2(Day),cook(Day,souvla), []):-specialOccasion,not(allergyMeat). %if it is a special occasion you can cook souvla.
rule(r3(Day),cook(Day,legumes),[]):-fasting.  %if it is fasting period you should make legumes.
rule(r4(Day),cook(Day,molluscs),[]):-fasting,specialOccasion,not(allergyMolluscs). %if it is fasting period AND a special occasion you can cook molluscs.
rule(r5(Day),cook(Day,legumes),[]):-allergyMeat.
rule(r6(Day),cook(Day,molluscs),[]):-allergyMeat,not(allergyMolluscs).

rule(pr0(X),prefer(r2(X),r1(X)),[]). %when you can cook souvla than regular meat , prefer souvla.
rule(pr1(X),prefer(r1(X),r2(X)),[]):-noMoney. %if you dont have money prefer to cook the cheaper 'regular' meat than souvla.
rule(pr2(X),prefer(pr1(X),pr0(X)),[]). %always prefer to satisfy the pr1 than pr0(if the noMoney predicate is true, else go with the pr0).

rule(pr4(X),prefer(r3(X),r1(X)),[]).%prefer to cook legumes than meat because if you have both options it means that is fasting period.
rule(pr3(X),prefer(r4(X),r2(X)),[]).%if you can cook mollusks and souvla , prefer mollusks because it means is a fasting period.

rule(pr5(X),prefer(r4(X),r3(X)),[]).%if you can cook legumes and mollusks prefer mollusks, they are better :p
rule(pr6(X),prefer(r3(X),r4(X)),[]):-noMoney.%if you dont have money for mollusks, go back to the legumes.

rule(pr7(X),prefer(pr6(X),pr5(X)),[]).%always prefer to check your money than cook mollusks without money,if you have money you will cook mollusks.

rule(pr8(X),prefer(r5(X),r6(X)),[]).%if you can cook legumes and mollusks prefer mollusks prefer legumes if you have an allergy and is not a special occasion.
rule(pr9(X),prefer(r6(X),r5(X)),[]):-specialOccasion.%if its a special occasion and you are allergic to meat do mollusks.

rule(pr10(X),prefer(pr9(X),pr8(X)),[]).%always prefer the cheapest if it is not a special occasion.
rule(pr11(X),prefer(r3(X),r2(X)),[]).%if you can do legumes and souvla and its fasting prefer legumes.



