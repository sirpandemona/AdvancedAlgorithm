results = table2array(results2);
%resultsold = table2array(results1);
%results = table2array(resultsunGen);

greedy = results(:, [4 5]);
bestfirst = results(:, [6 7]);
lawlerOpt = results(:,[8 9]);
lawlerApx = results(:,[10 11]);

problemsizes = results(:, [1 4 5 6 7 8 9 10 11]);
nvals = unique(problemsizes(:,1))';
psarr = {};
meanps = [];
maxps = [];
minps = [];
stdps = [];
for j = 1:size(nvals,2)
    n = nvals(j);
    m = problemsizes(find(problemsizes(:,1) ==n),2:end);
    psarr{j} =m; 
    meanps(:,j) = mean(m);
    maxps(:,j) = max(m);
    minps(:,j) = min(m);
    stdps(:,j)  = std(m);
end

figure;
hold on;
p1=  plot(nvals,meanps(2,:)','g');    
plot(nvals,maxps(2,:)', 'g--');
plot(nvals,minps(2,:)', 'g--');
p2=plot(nvals,meanps(4,:)','r');
plot(nvals,maxps(4,:)', 'r--');
plot(nvals,minps(4,:)', 'r--');
p3=plot(nvals,meanps(6,:)','b');
plot(nvals,maxps(6,:)', 'b--');
plot(nvals,minps(6,:)', 'b--');
p4=plot(nvals,meanps(8,:)','c');
plot(nvals,maxps(8,:)', 'c--');
plot(nvals,minps(8,:)', 'c--');
xlabel('n')
ylabel('ms')
hold off;
legend([p1 p2 p3 p4],{'Greedy','Best First', 'Lawler-DP', 'Lawler-Apx'});
set(gca,'YMinorTick','on','YScale','log');
title("Runtime v.s. problem size")

figure;
hold on;
p1=  plot(nvals,meanps(1,:)','g');
plot(nvals,maxps(1,:)', 'g--');
plot(nvals,minps(1,:)', 'g--');
p2=plot(nvals,meanps(3,:)','r');
plot(nvals,maxps(3,:)', 'r--');
plot(nvals,minps(3,:)', 'r--');
p3=plot(nvals,meanps(5,:)','b');
plot(nvals,maxps(5,:)', 'b--');
plot(nvals,minps(5,:)', 'b--');
p4=plot(nvals,meanps(7,:)','c');
plot(nvals,maxps(7,:)', 'c--');
plot(nvals,minps(7,:)', 'c--');
xlabel('n')
ylabel('Tard')
hold off;
legend([p1 p2 p3 p4],{'Greedy','Best First', 'Lawler-DP', 'Lawler-Apx'});
set(gca,'YMinorTick','on','YScale','lin');
title("Tardiness Score v.s. problem size ");

relativeduedates = results(:, [1 2 4 5 6 7 8 9 10 11]);
relativeduedates = relativeduedates(find(relativeduedates(:,1) ==10),2:end); %only get size = 10
rdvals = unique(relativeduedates(:,1))';
rdarr = {};
meanrd = [];
maxrd = [];
minrd = [];
stdrd = [];
for j = 1:size(rdvals,2)
    rd = rdvals(j);
    m = relativeduedates(find(relativeduedates(:,1) ==rd),2:end);
    rdarr{j} =m; 
    meanrd(:,j) = mean(m);
    maxrd(:,j) = max(m);
    minrd(:,j) = min(m);
    stdrd(:,j) = std(m);
end

figure;
hold on;
p1=  plot(rdvals,meanrd(2,:)','g');    
plot(rdvals,maxrd(2,:)', 'g--');
plot(rdvals,minrd(2,:)', 'g--');
p2=plot(rdvals,meanrd(4,:)','r');
plot(rdvals,maxrd(4,:)', 'r--');
plot(rdvals,minrd(4,:)', 'r--');
p3=plot(rdvals,meanrd(6,:)','b');
plot(rdvals,maxrd(6,:)', 'b--');
plot(rdvals,minrd(6,:)', 'b--');
p4=plot(rdvals,meanrd(8,:)','c');
plot(rdvals,maxrd(8,:)', 'c--');
plot(rdvals,minrd(8,:)', 'c--');
xlabel('RDD')
ylabel('ms')
hold off;
legend([p1 p2 p3 p4],{'Greedy','Best First', 'Lawler-DP', 'Lawler-Apx'});
set(gca,'YMinorTick','on','YScale','log');
title("Runtime v.s. Relative Due Date ");

figure;
hold on;
p1=  plot(rdvals,meanrd(1,:)','g');
plot(rdvals,maxrd(1,:)', 'g--');
plot(rdvals,minrd(1,:)', 'g--');
p2=plot(rdvals,meanrd(3,:)','r');
plot(rdvals,maxrd(3,:)', 'r--');
plot(rdvals,minrd(3,:)', 'r--');
p3=plot(rdvals,meanrd(5,:)','b');
plot(rdvals,maxrd(5,:)', 'b--');
plot(rdvals,minrd(5,:)', 'b--');
p4=plot(rdvals,meanrd(7,:)','c');
plot(rdvals,maxrd(7,:)', 'c--');
plot(rdvals,minrd(7,:)', 'c--');
xlabel('RRD')
ylabel('Tard')
hold off;
legend([p1 p2 p3 p4],{'Greedy','Best First', 'Lawler-DP', 'Lawler-Apx'});
set(gca,'YMinorTick','on','YScale','lin');
title("Tardiness Score v.s. Relative Due Date");

tardinessfactor = results(:, [1 3 4 5 6 7 8 9 10 11]);
tardinessfactor = tardinessfactor(find(tardinessfactor(:,1) ==10),2:end); %only get n=10
tfvals = unique(tardinessfactor(:,1))';
tfarr = {};
meantf = [];
maxtf = [];
mintf = [];
stdtf = [];
for j = 1:size(tfvals,2)
    tf = tfvals(j);
    m = tardinessfactor(find(tardinessfactor(:,1) ==tf),2:end);
    tfarr{j} =m; 
    meantf(:,j) = mean(m);
    maxtf(:,j) = max(m);
    mintf(:,j) = min(m);
    stdtf(:,j) = std(m);
end
M = []
for j = 1:size(tfvals,2)
   for k = 1: size(rdvals,2)
        tf = tfvals(j);
        rd = rdvals(k);
        m = results(find((results(:,2) == rd) & (results(:,3) == tf)),7);
        %M = [M;[tf rd mean(m)]];
        M(j,k) = mean(m);
   end
end


figure;
hold on;
p1=  plot(tfvals,meantf(2,:)','g');    
plot(tfvals,maxtf(2,:)', 'g--');
plot(tfvals,mintf(2,:)', 'g--');
p2=plot(tfvals,meantf(4,:)','r');
plot(tfvals,maxtf(4,:)', 'r--');
plot(tfvals,mintf(4,:)', 'r--');
p3=plot(tfvals,meantf(6,:)','b');
plot(tfvals,maxtf(6,:)', 'b--');
plot(tfvals,mintf(6,:)', 'b--');
p4=plot(tfvals,meantf(8,:)','c');
plot(tfvals,maxtf(8,:)', 'c--');
plot(tfvals,mintf(8,:)', 'c--');
xlabel('TF')
ylabel('ms')
hold off;
legend([p1 p2 p3 p4],{'Greedy','Best First', 'Lawler-DP', 'Lawler-Apx'});
set(gca,'YMinorTick','on','YScale','log');
title("Runtime v.s. Tardiness Factor ");

figure;
hold on;
p1=  plot(tfvals,meantf(1,:)','g');
plot(tfvals,maxtf(1,:)', 'g--');
plot(tfvals,mintf(1,:)', 'g--');
p2=plot(tfvals,meantf(3,:)','r');
plot(tfvals,maxtf(3,:)', 'r--');
plot(tfvals,mintf(3,:)', 'r--');
p3=plot(tfvals,meantf(5,:)','b');
plot(tfvals,maxtf(5,:)', 'b--');
plot(tfvals,mintf(5,:)', 'b--');
p4=plot(tfvals,meantf(7,:)','c');
plot(tfvals,maxtf(7,:)', 'c--');
plot(tfvals,mintf(7,:)', 'c--');
xlabel('TF')
ylabel('Tard')
hold off;
legend([p1 p2 p3 p4],{'Greedy','Best First', 'Lawler-DP', 'Lawler-Apx'});
set(gca,'YMinorTick','on','YScale','lin');
title("Tardiness Score v.s. Tardiness Factor");
