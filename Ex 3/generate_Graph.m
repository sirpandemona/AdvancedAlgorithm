function G = generate_Graph(n,p,w)
    G = [];
    for n1 = 1 : n
        for n2 = 1 : n1
            if (n1 ~= n2)
                rnd = rand();
                if (rnd < p)
                    wx = floor(rand()*w) +1;
                    G = [G; n2 n1 wx];
                end
            end
        end
    end
end