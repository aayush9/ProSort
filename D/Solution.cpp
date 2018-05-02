#include "bits/stdc++.h"
using namespace std;
const int N = 505;
const int mod = 1e9 + 7;
int n , h;
int dp[N][N];
int solve(int n , int d){
	if(d < 1){
		return 0;
	}
	if(n <= 1){
		return 1;
	}
	if(dp[n][d] != -1){
		return dp[n][d];
	}
	long long res = 0;
	for(int i = 1 ; i <= n ; ++i){
		int lft = i - 1;
		int rgt = n - i;
		int tmp = (1LL * solve(lft , d - 1) * solve(rgt , d - 1)) % mod;
		res += tmp;
	}
	return dp[n][d] = res % mod;
}
int main(){
	scanf("%d %d" , &n , &h);
	int ans = 0;
	memset(dp , -1 , sizeof(dp));
	ans += solve(n , h);
	ans -= solve(n , h - 1);
	if(ans < 0){
		ans += mod;
	}
	printf("%d\n" , ans);
}