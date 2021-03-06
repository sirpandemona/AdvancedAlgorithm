%init variables
%W = [1 5 10 25 50 100];
N = [5 10 20 30 40 50 60 70];
W = [1 5 25 50 100 250];
%N = [5 10 15 20 25 30 40 50];
I = 5;
P = [0.5 1];
T = [5 10 20 30 40 50 60];
genNewData = false;

%get size of variable vectors
pl = size(P,2);
wl = size(W,2);
nl = size(N,2);
tl = size(T,2);
%init cell matrix to store results
%results = cell(I,wl,nl,pl,tl);
%loop over all variable indices
if (genNewData)
    lbs = [];
    ubs = [];
    rts = [];
for i = 1: I
for iw = 1 : wl
for in = 1:nl
for ip = 1:pl
for it = 1: tl
    %init actual value of variable
    w = W(iw);
    n = N(in);
    p = P(ip);
    t = T(it);
    
    G = generate_Graph(n,p,w);
    tic;
    [lowerbound,upperbound] = randomizedRounding(n,G,t);
    runtime = toc;
    %store results
    lbs(i,iw,in,ip,it) = lowerbound;
    ubs(i,iw,in,ip,it) = upperbound;
    rts(i,iw,in,ip,it) = runtime;

    %results{i,iw,in,ip,it} = [lowerbounds, upperbounds ,runtime];
end
end
end
end
end
end
close all;
%results are I*W*N*P*T sized
mubs = squeeze(mean(ubs));
mlbs = squeeze(mean(lbs));
mrts = squeeze(mean(rts));

%plot runtime against n for diff p
nprts = squeeze(mean(mrts));
nprts = squeeze(mean(nprts,3));
%plot(N,nprts);
createfigure(N, nprts, {'Runtime against amount of nodes for different p'}, {'N'},{'Runtime(s)'}, {'p = 0.5', 'p = 1'})

%plot runtime against n for diff w
nwrts = squeeze(mean(mrts,3));
nwrts = squeeze(mean(nwrts,3));
%plot(N,nwrts);
createfigure(N, nwrts, {'Runtime against amount of nodes for different W'}, {'N'},{'Runtime(s)'}, {'W=1','W = 5', 'W =25','W=50','W=100','W=250'})

%plot runtime against t
trts = squeeze(mean(mrts));
trts = squeeze(mean(trts));
trts = squeeze(mean(trts));
%plot(T,trts);
createfigure(T, trts, {'Runtime against T'}, {'T'},{'Runtime(s)'}, {''})


%plot lower + upperbound against n

nlbs = squeeze(mean(mlbs));
nlbs = squeeze(mean(nlbs,3));
nlbs = squeeze(mean(nlbs,2));

nubs = squeeze(mean(mubs));
nubs = squeeze(mean(nubs,3));
nubs = squeeze(mean(nubs,2));
%plot(N,[nubs nlbs]);
createfigure(N, [nubs nlbs], {'Value of upper and lower bounds against amount of nodes '}, {'N'},{'Value'}, {'upper bound', 'lower bound'})

%plot lower + upperbound against w

wlbs = squeeze(mean(mlbs,3));
wlbs = squeeze(mean(wlbs,3));
wlbs = squeeze(mean(wlbs,2));

wubs = squeeze(mean(mubs,3));
wubs = squeeze(mean(wubs,3));
wubs = squeeze(mean(wubs,2));
%plot(W,[wubs wlbs]);
createfigure(W, [wubs wlbs], {'Value of upper and lower bounds against weight '}, {'W'},{'Value'}, {'upper bound', 'lower bound'})

%plot lower + upperbound against T

tlbs = squeeze(mean(mlbs));
tlbs = squeeze(mean(tlbs));
tlbs = squeeze(mean(tlbs));

tubs = squeeze(mean(mubs));
tubs = squeeze(mean(tubs));
tubs = squeeze(mean(tubs));
%plot(T,[tubs' tlbs']);
createfigure(T, [tubs tlbs], {'Value of upper and lower bounds against T '}, {'T'},{'Value'}, {'upper bound', 'lower bound'})

function createfigure(X1, YMatrix1, tit, xlab,ylab, displaynames)
figure1 = figure;
axes1 = axes('Parent',figure1);
hold(axes1,'on');
plot1 = plot(X1,YMatrix1);
xlabel(xlab);
title(tit);
ylabel(ylab);
box(axes1,'on');
legend(axes1,'Show',displaynames );
end
