#include <bits/stdc++.h>
#include "testlib.h"

using namespace std;

int main(int argc, char* argv[]){
    registerValidation(argc, argv);
    int T = inf.readInt(1,100000,"T");
    inf.readEoln();
    for(int t=0;t<T;++t){
        int p = inf.readInt(1,1000000000,"p");
        inf.readSpace();
        int q = inf.readInt(1,1000000000,"q");
        inf.readEoln();
    }
    inf.readEof();
}