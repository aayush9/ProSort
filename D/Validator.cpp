#include <bits/stdc++.h>
#include "testlib.h"

using namespace std;
int main(int argc, char* argv[]){
    registerValidation(argc, argv);
    int N = inf.readInt(1,500,"N");
    inf.readSpace();
    int H = inf.readInt(1,500,"M");
    inf.readEoln();
    ensure(H<=N);
    inf.readEof();
}