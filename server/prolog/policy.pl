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
% Scenarios: <1, {}, cook(meat)> <2, {specialOccasion(Day}, cook(
% souvla)> <3, {fastingPeriod}, cook( legumes)> <4,
% {fastingPeriod,specialOccasion}, cook( molluscs)> <5,
% {noMoney}, cook(meat)> <6, {fastingPeriod,noMoney },
% cook( legumes)> <7,
% {allergyMeat},cook(legumes);cook(mollusks)> <8,
% {allergyMollusks},cook( meat);cook(souvla);cook(legumes)>
% <9, {allergyMollusks,allergyMeat},cook(legumes)>
%-------------------------------------------------------------------------
%

%add gorgias to the program
:- dynamic allergyMeat/0, allergyMolluscs/0, noMoney/0,specialOccasion/0,fasting/0,nameday/1,money/1,vegetarianFood/1,wantMeat/0.
:- compile('gorgias-src-0.6d/lib/gorgias').
:- compile('gorgias-src-0.6d/ext/lpwnf').

%Write here the known facts about your world(sensors).

complement(cook(souvla),cook(meat)).
complement(cook(souvla),cook(molluscs)).
complement(cook(souvla),cook(legumes)).
complement(cook(meat),cook(souvla)).
complement(cook(meat),cook(legumes)).
complement(cook(meat),cook(molluscs)).
complement(cook(legumes),cook(molluscs)).
complement(cook(legumes),cook(meat)).
complement(cook(legumes),cook(souvla)).
complement(cook(molluscs),cook(legumes)).
complement(cook(molluscs),cook(souvla)).
complement(cook(molluscs),cook(meat)).


rule(r1,cook(meat),[]).
rule(r2,cook(souvla), []):-specialOccasion. 
rule(r3,cook(legumes),[]).  	
rule(r4,cook(molluscs),[]):-specialOccasion. 


rule(pr5,prefer(r3,r4),[]):-allergyMolluscs.

rule(pr6,prefer(r3,r1),[]):-vegetarianFood.

rule(pr7,prefer(r3,r2),[]):-vegetarianFood.

rule(pr8,prefer(r4,r1),[]):-vegetarianFood.

rule(pr9,prefer(r4,r2),[]):-vegetarianFood.