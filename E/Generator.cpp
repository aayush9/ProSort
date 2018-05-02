#include <bits/stdc++.h>
#include "testlib.h"
using namespace std;
int main(int argc, char* argv[]){
	registerGen(argc,argv,1);
	cout<<1<<rnd.next("[0-1]{1,99999}")<<endl;
}