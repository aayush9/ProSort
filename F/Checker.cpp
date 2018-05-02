#include <bits/stdc++.h>
#include "testlib.h"
using namespace std;
set<pair<int,int>> edges;
int n,m;
inline int readAndCheckAnswer(InStream& in) {
    int result = in.readInt(0, n/2);

    set<int> students;

    for (int i = 0; i < result; i++) {
        int x = in.readInt(1, n);
        int y = in.readInt(1, n);
        
        if(students.find(x)!=students.end() || students.find(y)!=students.end())
        	in.quitf(_wa,"One student can't be part of 2 groups");
        
        if (edges.find({x,y})==edges.end())
            in.quitf(_wa, "Students not friends"); 
        students.insert(x);
        students.insert(y);
    }
    return result;
}

int main(int argc, char* argv[]) {
    registerTestlibCmd(argc, argv);
    
    n = inf.readInt();
    m = inf.readInt();
    for(int x,y;m--;){
    	x = inf.readInt();
    	y = inf.readInt();
    	edges.insert({x,y}); edges.insert({y,x});
    }

    int ja = readAndCheckAnswer(ans);
    int pa = readAndCheckAnswer(ouf);

    if(ja != pa)
    	quitf(_wa, "Jury has a different answer");

    quitf(_ok, "k=%d", pa);
}