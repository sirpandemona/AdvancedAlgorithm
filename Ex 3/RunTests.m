%init variables
%W = [1 5 10 25 50 100];
%N = [5 10 20 30 40 50 60 70];
W = [5 25 100];
N = [5 10 25 40];
I = 2;
P = [0.5 1];

%get size of variable vectors
pl = size(P,2);
wl = size(W,2);
nl = size(N,2);

%init cell matrix to store results
results = cell(il,wl,nl,pl);
%loop over all variable indices
for i = 1: I
for iw = 1 : wl
for in = 1:nl
for ip = 1:pl
    %init actual value of variable
    w = W(iw);
    n = N(in);
    p = P(ip);
    
    G = generate_Graph(n,p,w);
    tic;
    [lowerbounds,upperbounds, T] = randomizedRounding(n,G);
    runtime = toc
    %store results
    results{i,iw,in,ip} = [lowerbounds, upperbounds,T ,runtime];
end
end
end
end