%results = table2array(results);
results = table2array(results1);

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
for n = nvals
    m = problemsizes(find(problemsizes(:,1) ==n),2:end);
    psarr{n} =m; 
    meanps(:,n) = mean(m);
    maxps(:,n) = max(m);
    minps(:,n) = min(m);
end

figure;
hold on;
p1=  plot(meanps(2,:)','g');    
plot(maxps(2,:)', 'g--');
plot(minps(2,:)', 'g--');
p2=plot(meanps(4,:)','r');
plot(maxps(4,:)', 'r--');
plot(minps(4,:)', 'r--');
p3=plot(meanps(6,:)','b');
plot(maxps(6,:)', 'b--');
plot(minps(6,:)', 'b--');
p4=plot(meanps(8,:)','c');
plot(maxps(8,:)', 'c--');
plot(minps(8,:)', 'c--');
xlabel('n')
ylabel('ms')
hold off;
legend([p1 p2 p3 p4],{'Greedy','Best First', 'Lawler-DP', 'Lawler-Apx'});
set(gca,'YMinorTick','on','YScale','log');
title("Runtime v.s. problem size")

figure;
hold on;
p1=  plot(meanps(1,:)','g');
plot(maxps(1,:)', 'g--');
plot(minps(1,:)', 'g--');
p2=plot(meanps(3,:)','r');
plot(maxps(3,:)', 'r--');
plot(minps(3,:)', 'r--');
p3=plot(meanps(5,:)','b');
plot(maxps(5,:)', 'b--');
plot(minps(5,:)', 'b--');
p4=plot(meanps(7,:)','c');
plot(maxps(7,:)', 'c--');
plot(minps(7,:)', 'c--');
xlabel('n')
ylabel('Tard')
hold off;
legend([p1 p2 p3 p4],{'Greedy','Best First', 'Lawler-DP', 'Lawler-Apx'});
set(gca,'YMinorTick','on','YScale','lin');
title("Tardiness Score v.s. problem size ");

relativeduedates = results(:, [2 4 5 6 7 8 9 10 11]);
rdvals = unique(relativeduedates(:,1))';
rdarr = {};
meanrd = [];
maxrd = [];
minrd = [];

for j = 1:size(rdvals,2)
    rd = rdvals(j);
    m = relativeduedates(find(relativeduedates(:,1) ==rd),2:end);
    rdarr{j} =m; 
    meanrd(:,j) = mean(m);
    maxrd(:,j) = max(m);
    minrd(:,j) = min(m);
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

tardinessfactor = results(:, [3 4 5 6 7 8 9 10 11]);

tfvals = unique(tardinessfactor(:,1))';
tfarr = {};
meantf = [];
maxtf = [];
mintf = [];

for j = 1:size(tfvals,2)
    tf = tfvals(j);
    m = tardinessfactor(find(tardinessfactor(:,1) ==tf),2:end);
    tfarr{j} =m; 
    meantf(:,j) = mean(m);
    maxtf(:,j) = max(m);
    mintf(:,j) = min(m);
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
