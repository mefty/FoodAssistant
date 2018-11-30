:-consult('./environment.pl').

rule(e1,prefer(r2,r1),[]). 
rule(e2,prefer(r1,r2),[]):-noMoney. 

rule(e3,prefer(r4,r3),[]).
rule(e4,prefer(r3,r4),[]):-noMoney.

rule(c1,prefer(e2,e1),[]). 
rule(c2,prefer(e4,e3),[]).
rule(c3,prefer(pr5,e3),[]). 

rule(m1,prefer(r1,r3),[]):-wantMeat. 
rule(m2,prefer(r1,r4),[]):-wantMeat.
 
rule(m3,prefer(r2,r3),[]):-wantMeat. 
rule(m4,prefer(r2,r4),[]):-wantMeat. 

rule(d1,prefer(pr6,m1),[]). 
rule(d2,prefer(pr8,m2),[]).
rule(d3,prefer(pr7,m3),[]).
rule(d4,prefer(pr9,m4),[]).