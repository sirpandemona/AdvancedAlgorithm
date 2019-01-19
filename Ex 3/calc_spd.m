function [A,h] = calc_spd(n,G,w)
    ops = sdpsettings('solver','sedumi', 'verbose',0);
    A = sdpvar(n,n,'symmetric');
    WA = w-w.*A;
    h = 0.5*sum(sum(WA));
    c = [diag(A) == ones(n,1), A >= 0];

    %solvesdp(c, -h, ops);
    optimize(c, -h, ops);
    A = double(A);
    h = double(h);
end