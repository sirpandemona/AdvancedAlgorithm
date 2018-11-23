results = table2array(results);
greedy = results(:, [4 5]);
bestfirst = results(:, [6 7]);
lawlerOpt = results(:,[8 9]);
lawlerApx = results(:,[10 11]);

size = results(:,1);
rdd = results(:,2);
tf = results(:,3);

problemsizes = results(:, [1 5 7 9 11]);
relativeduedates = results(:, [2 5 7 9 11]);
tardinessfactor = results(:, [3 5 7 9 11]);