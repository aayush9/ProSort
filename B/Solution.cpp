#include <bits/stdc++.h>

using namespace std;

int main()
{
	int K, S;
	cin >> K >> S;

	long long int two_sum[3 * K + 1];
	memset(two_sum, 0, sizeof(two_sum));

	for (int i = 0; i <= K; i++)
		for (int j = 0; j <= K; j++)
			two_sum[i + j]++;

	long long int ans = 0;
	for (int k = 0; k <= K and k <= S; k++)
		ans += two_sum[S - k];

	cout << ans << endl;

	return 0;
}