function [A,h,t] = calc_spd(n,G)
    w = zeros(n,n);
    for e = 1:size(G,1)
        x = G(e,:);
        w(x(1),x(2)) = x(3);
    end
    ops = sdpsettings('solver','sedumi');
    A = sdpvar(n,n);
    WA = w-w*A;
    h = 0.5*sum(sum(tril(WA)));
    c = [diag(A) == ones(n,1), A >= 0];
    sol = solvesdp(c, -h, ops);
    A = double(A)
    h = double(-h)
    t = sol.solvertime
end