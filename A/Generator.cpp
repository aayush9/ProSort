#include <bits/stdc++.h>
#include "testlib.h"
using namespace std;
int main(int argc, char* argv[]){
	registerGen(argc,argv,1);
	int T=rnd.next(1,100000);
	cout<<T<<endl;
	for(int t=0;t<T;++t){
		int p = rnd.next(1,1000000000);
		int q = rnd.next(1,1000000000);
		cout<<p<<' '<<q<<endl;
	}
}