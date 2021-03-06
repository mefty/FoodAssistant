
% A person wants to find out if her security interest in a certain ship
% is perfected. She currently has possession of the ship. According to the 
% "Uniform Commercial Code" (UCC) a security interest in goods may be perfected
% by taking possession of the collateral. However, there is a federal law 
% called the "Ship Mortgage Act" (SMA) according to which a security interest 
%  in a ship may only be perfected by filing a financing statement. Such a 
% statement has not been filed. Now the question is whether the UCC or the SMA 
% takes precedence in this case. There are two known legal principles for 
% resolving conflicts of this kind. The principle of "Lex Posterior" gives 
% precedence to newer laws. In our case the UCC is newer than the SMA. On the 
% other hand, in the principle of "Lex Superior" gives precedence to laws 
% supported by the higher authority. In our case the SMA has higher authority
% since it is a federal law."
% -- Gordon, 1993


:- compile('../lib/gorgias').
:- compile('../ext/lpwnf').


rule(ucc, perfected,      [possession]).
rule(sma, neg(perfected), [ship, neg(finstatement)]).

rule(f1, possession,        []).
rule(f2, ship,              []).
rule(f3, neg(finstatement), []).
rule(f4, newer(ucc,sma),    []).
rule(f5, federal_law(sma),  []).
rule(f6, state_law(ucc),    []).

rule(lex_posterior(X,Y), prefer(X,Y), [newer(X,Y)]). 
rule(lex_superior(X,Y),  prefer(Y,X), [state_law(X),federal_law(Y)]).

rule(prpr, prefer(lex_superior(X,Y),lex_posterior(X,Y)), []).

