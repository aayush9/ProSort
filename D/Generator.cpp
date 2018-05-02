#include <bits/stdc++.h>
#include "testlib.h"
using namespace std;
int main(int argc, char* argv[]){
	registerGen(argc,argv,1);
	int N = rnd.next(1,500);
	int H = rnd.next(1,N);
	cout<<N<<' '<<H<<endl;
}