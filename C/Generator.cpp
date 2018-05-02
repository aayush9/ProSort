#include <bits/stdc++.h>
#include "testlib.h"
using namespace std;
int main(int argc, char* argv[]){
	registerGen(argc,argv,1);
	int k=rnd.next(1,1000000);
	int n=rnd.next(k,1000000);
	cout<<n<<' '<<k<<endl;
}