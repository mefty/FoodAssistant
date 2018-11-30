specialOccasion:-nameday(sunday).
specialOccasion:-day(25),month(12).
specialOccasion:-day(1),month(1).
specialOccasion:-day(19),month(2),year(2018).
specialOccasion:-day(15),month(19),year(2018).

fasting:-nameday(wednesday).
fasting:-nameday(friday).
fasting:-day(X),between(19,28,X),month(2),year(2018).
fasting:-day(X),between(1,31,X),month(3),year(2018).
fasting:-day(X),between(1,7,X),month(4),year(2018).
fasting:-day(X),between(1,14,X),month(8).
fasting:-day(X),between(14,30,X),month(11).
fasting:-day(X),between(1,24,X),month(12).


vegetarianFood:-fasting.
vegetarianFood:-allergyMeat.

noMoney:-money(X),X<10.

:-consult('./policy.pl').

