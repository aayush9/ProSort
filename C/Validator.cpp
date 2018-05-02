#include <bits/stdc++.h>
#include "testlib.h"
using namespace std;
int main(int argc, char* argv[]){
    registerValidation(argc, argv);
    int n = inf.readInt(1,1000000,"N");
    inf.readSpace();
    int k = inf.readInt(1,1000000,"M");
    ensure(n>=k);
    inf.readEoln();
    inf.readEof();
}