:- compile('../../lib/gorgias').
:- compile('../../ext/lpwnf').

rule(f1, subclass(a,b), []).
rule(f2, subclass(c,b), []).
rule(f3, subclass(d,c), []).
rule(f4, is_in(x1,a),   []).
rule(f5, is_in(x2,c),   []).
rule(f6, is_in(x3,d),   []).


rule(d1(X), has(X,p),      [is_in(X,b)]).
rule(d2(X), neg(has(X,p)), [is_in(X, c)]).
rule(d3(X), prefer(d2(X),d1(X)), [is_in(X, c)]).


% General properties of subclass and is_in

rule(r1(C0,C2), subclass(C0,C2), [C0 \= C1, C1 \= C2, C0 \= C2, subclass(C0,C1), subclass(C1,C2)]).
rule(r2(X,C1),  is_in(X,C1),      [subclass(C0,C1), is_in(X,C0)]).


% Closed world assumptions for simple hierarchies

rule(cwa1(X,C), neg(is_in(X, C)),   []).
rule(cwa2(A,B), neg(subclass(A,B)), []).


%%%% UNCOMMENT BELOW TO TEST HIGHER-ORDER PRIORITIES
% rule(f7, subclass(d,a), []).
% rule(d4(X),   prefer(d1(X),d2(X)), [is_in(X,a)]).



start :-
	writeln('======================================================'),
	writeln('Inheritance with Exceptions'),
	writeln('======================================================'),
	test1,
	writeln('------------------------------------------------------'),
	test21,
	writeln('------------------------------------------------------'),
	test22,
	writeln('------------------------------------------------------'),
	test3.

test1 :-
	Query = [has(x1,p)],
	writeln('Test 1'),
	writeln(''),
	writeln('  Query:'),
	pretty(Query),
	prove(Query,Delta),
	writeln('  Delta:'),
	pretty(Delta).

test21 :-
	Query = [has(x2,p)],
	writeln('Test 2.1'),
	writeln(''),
	writeln('  Query:'),
	pretty(Query),
	prove(Query,Delta),
	writeln('  Delta:'),
	pretty(Delta).

test21.

test22 :-
	Query = [neg(has(x2,p))],
	writeln('Test 2.2'),
	writeln(''),
	writeln('  Query:'),
	pretty(Query),
	prove(Query,Delta),
	writeln('  Delta:'),
	pretty(Delta).

test3 :-
	Query = [has(x3,p)],
	writeln('Test 3'),
	writeln(''),
	writeln('  Query:'),
	pretty(Query),
	prove(Query,Delta),
	writeln('  Delta:'),
	pretty(Delta).

