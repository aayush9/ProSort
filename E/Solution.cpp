/*input
0
*/
#include <bits/stdc++.h>
using namespace std;
string s;
void output(int idx){
	for(int i=0;i<s.size();++i){
		if(i>=idx) s[i]=(s[i]=='1'?'0':'1');
	}
	int i=0;
	while(i<s.size()-1 && s[i]=='0') ++i;
	cout<<s.substr(i,s.size())<<'\n';
	exit(0);
}
int main(){
	ios_base::sync_with_stdio(0);
	cin>>s;
	s = "00" + s;
	for(int i=s.size(),x=0;i--;x^=1)
		if(s[i]-'0'==x) output(i);
}