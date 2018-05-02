import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";

	static void solve() {
		int n = ni(), K = ni();
		K = Math.min(K, n/2);
		int[][] fif = enumFIF(300000, mod);
		long[] a = new long[n+2];
		a[0] = 1;
		a[1] = mod-1;
		for(int i = 0;i < K;i++){
			a[2+i*2] =  -(C(2*i, i, mod, fif) - C(2*i, i+1, mod, fif));
			while(a[2+i*2] < 0)a[2+i*2] += mod;
		}
		long[] b = inv(a);
		out.println(b[n]);
	}
	
	public static long C(int n, int r, int mod, int[][] fif) {
		if (n < 0 || r < 0 || r > n)
			return 0;
		return (long) fif[0][n] * fif[1][r] % mod * fif[1][n - r] % mod;
	}

	
	public static int[][] enumFIF(int n, int mod) {
		int[] f = new int[n + 1];
		int[] invf = new int[n + 1];
		f[0] = 1;
		for (int i = 1; i <= n; i++) {
			f[i] = (int) ((long) f[i - 1] * i % mod);
		}
		long a = f[n];
		long b = mod;
		long p = 1, q = 0;
		while (b > 0) {
			long c = a / b;
			long d;
			d = a;
			a = b;
			b = d % b;
			d = p;
			p = q;
			q = d - c * q;
		}
		invf[n] = (int) (p < 0 ? p + mod : p);
		for (int i = n - 1; i >= 0; i--) {
			invf[i] = (int) ((long) invf[i + 1] * (i + 1) % mod);
		}
		return new int[][] { f, invf };
	}

	
	public static int mod = 1000000007;
	public static long[] mul(long[] a, long[] b)
	{
		if(a.length >= 3000){
			return Arrays.copyOf(convolute(a, b, 3, mod), a.length+b.length-1);
		}else{
			return mulnaive(a, b);
		}
	}
	
	public static long[] mul(long[] a, long[] b, int lim)
	{
		if(a.length >= 3000){
			return Arrays.copyOf(convolute(a, b, 3, mod), lim);
		}else{
			return mulnaive(a, b, lim);
		}
	}
	
	public static long[] mulnaive(long[] a, long[] b)
	{
		long[] c = new long[a.length+b.length-1];
		long big = 8L*mod*mod;
		for(int i = 0;i < a.length;i++){
			for(int j = 0;j < b.length;j++){
				c[i+j] += a[i]*b[j];
				if(c[i+j] >= big)c[i+j] -= big;
			}
		}
		for(int i = 0;i < c.length;i++)c[i] %= mod;
		return c;
	}
	
	public static long[] mulnaive(long[] a, long[] b, int lim)
	{
		long[] c = new long[lim];
		long big = 8L*mod*mod;
		for(int i = 0;i < a.length;i++){
			for(int j = 0;j < b.length && i+j < lim;j++){
				c[i+j] += a[i]*b[j];
				if(c[i+j] >= big)c[i+j] -= big;
			}
		}
		for(int i = 0;i < c.length;i++)c[i] %= mod;
		return c;
	}
	
	public static final int[] NTTPrimes = {1053818881, 1051721729, 1045430273, 1012924417, 1007681537, 1004535809, 998244353, 985661441, 976224257, 975175681};
	public static final int[] NTTPrimitiveRoots = {7, 6, 3, 5, 3, 3, 3, 3, 3, 17};
//	public static final int[] NTTPrimes = {1012924417, 1004535809, 998244353, 985661441, 975175681, 962592769, 950009857, 943718401, 935329793, 924844033};
//	public static final int[] NTTPrimitiveRoots = {5, 3, 3, 3, 17, 7, 7, 7, 3, 5};
	
	public static long[] convoluteSimply(long[] a, long[] b, int P, int g)
	{
		int m = Math.max(2, Integer.highestOneBit(Math.max(a.length, b.length)-1)<<2);
		long[] fa = nttmb(a, m, false, P, g);
		long[] fb = a == b ? fa : nttmb(b, m, false, P, g);
		for(int i = 0;i < m;i++){
			fa[i] = fa[i]*fb[i]%P;
		}
		return nttmb(fa, m, true, P, g);
	}
	
	public static long[] convolute(long[] a, long[] b)
	{
		int USE = 2;
		int m = Math.max(2, Integer.highestOneBit(Math.max(a.length, b.length)-1)<<2);
		long[][] fs = new long[USE][];
		for(int k = 0;k < USE;k++){
			int P = NTTPrimes[k], g = NTTPrimitiveRoots[k];
			long[] fa = nttmb(a, m, false, P, g);
			long[] fb = a == b ? fa : nttmb(b, m, false, P, g);
			for(int i = 0;i < m;i++){
				fa[i] = fa[i]*fb[i]%P;
			}
			fs[k] = nttmb(fa, m, true, P, g);
		}
		
		int[] mods = Arrays.copyOf(NTTPrimes, USE);
		long[] gammas = garnerPrepare(mods);
		int[] buf = new int[USE];
		for(int i = 0;i < fs[0].length;i++){
			for(int j = 0;j < USE;j++)buf[j] = (int)fs[j][i];
			long[] res = garnerBatch(buf, mods, gammas);
			long ret = 0;
			for(int j = res.length-1;j >= 0;j--)ret = ret * mods[j] + res[j];
			fs[0][i] = ret;
		}
		return fs[0];
	}
	
	public static long[] convolute(long[] a, long[] b, int USE, int mod)
	{
		int m = Math.max(2, Integer.highestOneBit(Math.max(a.length, b.length)-1)<<2);
		long[][] fs = new long[USE][];
		for(int k = 0;k < USE;k++){
			int P = NTTPrimes[k], g = NTTPrimitiveRoots[k];
			long[] fa = nttmb(a, m, false, P, g);
			long[] fb = a == b ? fa : nttmb(b, m, false, P, g);
			for(int i = 0;i < m;i++){
				fa[i] = fa[i]*fb[i]%P;
			}
			fs[k] = nttmb(fa, m, true, P, g);
		}
		
		int[] mods = Arrays.copyOf(NTTPrimes, USE);
		long[] gammas = garnerPrepare(mods);
		int[] buf = new int[USE];
		for(int i = 0;i < fs[0].length;i++){
			for(int j = 0;j < USE;j++)buf[j] = (int)fs[j][i];
			long[] res = garnerBatch(buf, mods, gammas);
			long ret = 0;
			for(int j = res.length-1;j >= 0;j--)ret = (ret * mods[j] + res[j]) % mod;
			fs[0][i] = ret;
		}
		return fs[0];
	}
	
	// static int[] wws = new int[270000]; // outer faster
	
	// Modifed Montgomery + Barrett
	private static long[] nttmb(long[] src, int n, boolean inverse, int P, int g)
	{
		long[] dst = Arrays.copyOf(src, n);
		
		int h = Integer.numberOfTrailingZeros(n);
		long K = Integer.highestOneBit(P)<<1;
		int H = Long.numberOfTrailingZeros(K)*2;
		long M = K*K/P;
		
		int[] wws = new int[1<<h-1];
		long dw = inverse ? pow(g, P-1-(P-1)/n, P) : pow(g, (P-1)/n, P);
		long w = (1L<<32)%P;
		for(int k = 0;k < 1<<h-1;k++){
			wws[k] = (int)w;
			w = modh(w*dw, M, H, P);
		}
		long J = invl(P, 1L<<32);
		for(int i = 0;i < h;i++){
			for(int j = 0;j < 1<<i;j++){
				for(int k = 0, s = j<<h-i, t = s|1<<h-i-1;k < 1<<h-i-1;k++,s++,t++){
					long u = (dst[s] - dst[t] + 2*P)*wws[k];
					dst[s] += dst[t];
					if(dst[s] >= 2*P)dst[s] -= 2*P;
//					long Q = (u&(1L<<32)-1)*J&(1L<<32)-1;
					long Q = (u<<32)*J>>>32;
					dst[t] = (u>>>32)-(Q*P>>>32)+P;
				}
			}
			if(i < h-1){
				for(int k = 0;k < 1<<h-i-2;k++)wws[k] = wws[k*2];
			}
		}
		for(int i = 0;i < n;i++){
			if(dst[i] >= P)dst[i] -= P;
		}
		for(int i = 0;i < n;i++){
			int rev = Integer.reverse(i)>>>-h;
			if(i < rev){
				long d = dst[i]; dst[i] = dst[rev]; dst[rev] = d;
			}
		}
		
		if(inverse){
			long in = invl(n, P);
			for(int i = 0;i < n;i++)dst[i] = modh(dst[i]*in, M, H, P);
		}
		
		return dst;
	}
	
	// Modified Shoup + Barrett
	private static long[] nttsb(long[] src, int n, boolean inverse, int P, int g)
	{
		long[] dst = Arrays.copyOf(src, n);
		
		int h = Integer.numberOfTrailingZeros(n);
		long K = Integer.highestOneBit(P)<<1;
		int H = Long.numberOfTrailingZeros(K)*2;
		long M = K*K/P;
		
		long dw = inverse ? pow(g, P-1-(P-1)/n, P) : pow(g, (P-1)/n, P);
		long[] wws = new long[1<<h-1];
		long[] ws = new long[1<<h-1];
		long w = 1;
		for(int k = 0;k < 1<<h-1;k++){
			wws[k] = (w<<32)/P;
			ws[k] = w;
			w = modh(w*dw, M, H, P);
		}
		for(int i = 0;i < h;i++){
			for(int j = 0;j < 1<<i;j++){
				for(int k = 0, s = j<<h-i, t = s|1<<h-i-1;k < 1<<h-i-1;k++,s++,t++){
					long ndsts = dst[s] + dst[t];
					if(ndsts >= 2*P)ndsts -= 2*P;
					long T = dst[s] - dst[t] + 2*P;
					long Q = wws[k]*T>>>32;
					dst[s] = ndsts;
					dst[t] = ws[k]*T-Q*P&(1L<<32)-1;
				}
			}
//			dw = dw * dw % P;
			if(i < h-1){
				for(int k = 0;k < 1<<h-i-2;k++){
					wws[k] = wws[k*2];
					ws[k] = ws[k*2];
				}
			}
		}
		for(int i = 0;i < n;i++){
			if(dst[i] >= P)dst[i] -= P;
		}
		for(int i = 0;i < n;i++){
			int rev = Integer.reverse(i)>>>-h;
			if(i < rev){
				long d = dst[i]; dst[i] = dst[rev]; dst[rev] = d;
			}
		}
		
		if(inverse){
			long in = invl(n, P);
			for(int i = 0;i < n;i++){
				dst[i] = modh(dst[i] * in, M, H, P);
			}
		}
		
		return dst;
	}
	
	static final long mask = (1L<<31)-1;
	
	public static long modh(long a, long M, int h, int mod)
	{
		long r = a-((M*(a&mask)>>>31)+M*(a>>>31)>>>h-31)*mod;
		return r < mod ? r : r-mod;
	}
	
	private static long[] garnerPrepare(int[] m)
	{
		int n = m.length;
		assert n == m.length;
		if(n == 0)return new long[0];
		long[] gamma = new long[n];
		for(int k = 1;k < n;k++){
			long prod = 1;
			for(int i = 0;i < k;i++){
				prod = prod * m[i] % m[k];
			}
			gamma[k] = invl(prod, m[k]);
		}
		return gamma;
	}
	
	private static long[] garnerBatch(int[] u, int[] m, long[] gamma)
	{
		int n = u.length;
		assert n == m.length;
		long[] v = new long[n];
		v[0] = u[0];
		for(int k = 1;k < n;k++){
			long temp = v[k-1];
			for(int j = k-2;j >= 0;j--){
				temp = (temp * m[j] + v[j]) % m[k];
			}
			v[k] = (u[k] - temp) * gamma[k] % m[k];
			if(v[k] < 0)v[k] += m[k];
		}
		return v;
	}
	
	public static long[] add(long[] a, long[] b)
	{
		long[] c = new long[Math.max(a.length, b.length)];
		for(int i = 0;i < a.length;i++)c[i] += a[i];
		for(int i = 0;i < b.length;i++)c[i] += b[i];
		for(int i = 0;i < c.length;i++)if(c[i] >= mod)c[i] -= mod;
		return c;
	}
	
	public static long[] add(long[] a, long[] b, int lim)
	{
		long[] c = new long[lim];
		for(int i = 0;i < a.length && i < lim;i++)c[i] += a[i];
		for(int i = 0;i < b.length && i < lim;i++)c[i] += b[i];
		for(int i = 0;i < c.length;i++)if(c[i] >= mod)c[i] -= mod;
		return c;
	}
	
	public static long[] sub(long[] a, long[] b)
	{
		long[] c = new long[Math.max(a.length, b.length)];
		for(int i = 0;i < a.length;i++)c[i] += a[i];
		for(int i = 0;i < b.length;i++)c[i] -= b[i];
		for(int i = 0;i < c.length;i++)if(c[i] < 0)c[i] += mod;
		return c;
	}
	
	public static long[] sub(long[] a, long[] b, int lim)
	{
		long[] c = new long[lim];
		for(int i = 0;i < a.length && i < lim;i++)c[i] += a[i];
		for(int i = 0;i < b.length && i < lim;i++)c[i] -= b[i];
		for(int i = 0;i < c.length;i++)if(c[i] < 0)c[i] += mod;
		return c;
	}
	
	// F_{t+1}(x) = -F_t(x)^2*P(x) + 2F_t(x)
	// if want p-destructive, comment out flipping p just before returning.
	public static long[] inv(long[] p)
	{
		int n = p.length;
		long[] f = {invl(p[0], mod)};
		for(int i = 0;i < p.length;i++){
			if(p[i] == 0)continue;
			p[i] = mod-p[i];
		}
		for(int i = 1;i < 2*n;i*=2){
			long[] f2 = mul(f, f, Math.min(n, 2*i));
			long[] f2p = mul(f2, Arrays.copyOf(p, i), Math.min(n, 2*i));
			for(int j = 0;j < f.length;j++){
				f2p[j] += 2L*f[j];
				if(f2p[j] >= mod)f2p[j] -= mod;
				if(f2p[j] >= mod)f2p[j] -= mod;
			}
			f = f2p;
		}
		for(int i = 0;i < p.length;i++){
			if(p[i] == 0)continue;
			p[i] = mod-p[i];
		}
		return f;
	}
	
	// differentiate
	public static long[] d(long[] p)
	{
		long[] q = new long[p.length];
		for(int i = 0;i < p.length-1;i++){
			q[i] = p[i+1] * (i+1) % mod;
		}
		return q;
	}
	
	// integrate
	public static long[] i(long[] p)
	{
		long[] q = new long[p.length];
		for(int i = 0;i < p.length-1;i++){
			q[i+1] = p[i] * invl(i+1, mod) % mod;
		}
		return q;
	}
	
	// F_{t+1}(x) = F_t(x)-(ln F_t(x) - P(x)) * F_t(x)
	public static long[] exp(long[] p)
	{
		int n = p.length;
		long[] f = {p[0]};
		for(int i = 1;i < 2*n;i*=2){
			long[] ii = ln(f);
			long[] sub = sub(ii, p, Math.min(n, 2*i));
			if(--sub[0] < 0)sub[0] += mod;
			for(int j = 0;j < 2*i && j < n;j++){
				sub[j] = mod-sub[j];
				if(sub[j] == mod)sub[j] = 0;
			}
			f = mul(sub, f, Math.min(n, 2*i));
//			f = sub(f, mul(sub(ii, p, 2*i), f, 2*i));
		}
		return f;
	}
	
	// \int f'(x)/f(x) dx
	public static long[] ln(long[] f)
	{
		long[] ret = i(mul(d(f), inv(f)));
		ret[0] = f[0];
		return ret;
	}
	
	// ln F(x) - k ln P(x) = 0
	public static long[] pow(long[] p, int K)
	{
		int n = p.length;
		long[] lnp = ln(p);
		for(int i = 1;i < lnp.length;i++)lnp[i] = lnp[i] * K % mod;
		lnp[0] = pow(p[0], K, mod); // go well for some reason
		return exp(Arrays.copyOf(lnp, n));
	}
	
	// destructive
	public static long[] divf(long[] a, int[][] fif)
	{
		for(int i = 0;i < a.length;i++)a[i] = a[i] * fif[1][i] % mod;
		return a;
	}
	
	// destructive
	public static long[] mulf(long[] a, int[][] fif)
	{
		for(int i = 0;i < a.length;i++)a[i] = a[i] * fif[0][i] % mod;
		return a;
	}
	
	public static long[] transformExponentially(long[] a, int[][] fif)
	{
		return mulf(exp(divf(Arrays.copyOf(a, a.length), fif)), fif);
	}
	
	public static long[] transformLogarithmically(long[] a, int[][] fif)
	{
		return mulf(Arrays.copyOf(ln(divf(Arrays.copyOf(a, a.length), fif)), a.length), fif);
	}
	
	public static long pow(long a, long n, long mod) {
		//		a %= mod;
		long ret = 1;
		int x = 63 - Long.numberOfLeadingZeros(n);
		for (; x >= 0; x--) {
			ret = ret * ret % mod;
			if (n << 63 - x < 0)
				ret = ret * a % mod;
		}
		return ret;
	}
	
	public static long invl(long a, long mod) {
		long b = mod;
		long p = 1, q = 0;
		while (b > 0) {
			long c = a / b;
			long d;
			d = a;
			a = b;
			b = d % b;
			d = p;
			p = q;
			q = d - c * q;
		}
		return p < 0 ? p + mod : p;
	}
	
	public static long[] reverse(long[] p)
	{
		long[] ret = new long[p.length];
		for(int i = 0;i < p.length;i++){
			ret[i] = p[p.length-1-i];
		}
		return ret;
	}
	
	public static long[] reverse(long[] p, int lim)
	{
		long[] ret = new long[lim];
		for(int i = 0;i < lim && i < p.length;i++){
			ret[i] = p[p.length-1-i];
		}
		return ret;
	}
	
	// [quotient, remainder]
	// remainder can be empty.
	// あとでしらべる
	public static long[][] div(long[] p, long[] q)
	{
		if(p.length < q.length)return new long[][]{new long[0], Arrays.copyOf(p, p.length)};
		long[] rp = reverse(p, p.length-q.length+1);
		long[] rq = reverse(q, p.length-q.length+1);
		long[] rd = mul(rp, inv(rq), p.length-q.length+1);
		long[] d = reverse(rd, q.length-1);
		long[] r = sub(p, mul(d, q, q.length-1), q.length-1);
		return new long[][]{d, r};
	}

	public static void main(String[] args) throws Exception {
		long S = System.currentTimeMillis();
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);

		solve();
		out.flush();
		long G = System.currentTimeMillis();
		tr(G - S + "ms");
	}

	private static boolean eof() {
		if (lenbuf == -1)
			return true;
		int lptr = ptrbuf;
		while (lptr < lenbuf)
			if (!isSpaceChar(inbuf[lptr++]))
				return false;

		try {
			is.mark(1000);
			while (true) {
				int b = is.read();
				if (b == -1) {
					is.reset();
					return true;
				} else if (!isSpaceChar(b)) {
					is.reset();
					return false;
				}
			}
		} catch (IOException e) {
			return true;
		}
	}

	private static byte[] inbuf = new byte[1024];
	static int lenbuf = 0, ptrbuf = 0;

	private static int readByte() {
		if (lenbuf == -1)
			throw new InputMismatchException();
		if (ptrbuf >= lenbuf) {
			ptrbuf = 0;
			try {
				lenbuf = is.read(inbuf);
			} catch (IOException e) {
				throw new InputMismatchException();
			}
			if (lenbuf <= 0)
				return -1;
		}
		return inbuf[ptrbuf++];
	}

	private static boolean isSpaceChar(int c) {
		return !(c >= 33 && c <= 126);
	}

	private static int skip() {
		int b;
		while ((b = readByte()) != -1 && isSpaceChar(b))
			;
		return b;
	}

	private static double nd() {
		return Double.parseDouble(ns());
	}

	private static char nc() {
		return (char) skip();
	}

	private static String ns() {
		int b = skip();
		StringBuilder sb = new StringBuilder();
		while (!(isSpaceChar(b))) { // when nextLine, (isSpaceChar(b) && b != '
									// ')
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}

	private static char[] ns(int n) {
		char[] buf = new char[n];
		int b = skip(), p = 0;
		while (p < n && !(isSpaceChar(b))) {
			buf[p++] = (char) b;
			b = readByte();
		}
		return n == p ? buf : Arrays.copyOf(buf, p);
	}

	private static char[][] nm(int n, int m) {
		char[][] map = new char[n][];
		for (int i = 0; i < n; i++)
			map[i] = ns(m);
		return map;
	}

	private static int[] na(int n) {
		int[] a = new int[n];
		for (int i = 0; i < n; i++)
			a[i] = ni();
		return a;
	}

	private static int ni() {
		int num = 0, b;
		boolean minus = false;
		while ((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'))
			;
		if (b == '-') {
			minus = true;
			b = readByte();
		}

		while (true) {
			if (b >= '0' && b <= '9') {
				num = num * 10 + (b - '0');
			} else {
				return minus ? -num : num;
			}
			b = readByte();
		}
	}

	private static long nl() {
		long num = 0;
		int b;
		boolean minus = false;
		while ((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'))
			;
		if (b == '-') {
			minus = true;
			b = readByte();
		}

		while (true) {
			if (b >= '0' && b <= '9') {
				num = num * 10 + (b - '0');
			} else {
				return minus ? -num : num;
			}
			b = readByte();
		}
	}

	private static void tr(Object... o) {
		if (INPUT.length() != 0)
			System.out.println(Arrays.deepToString(o));
	}
}