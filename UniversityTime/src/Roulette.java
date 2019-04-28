import java.util.Random;

class Roulette
{
    double[] c;
    double total;
    Random random;

    public Roulette(double[] n) {
        random = new Random();
        total = 0;
        c = new double[n.length+1];
        c[0] = 0;
        // Create cumulative values for later:
        for (int i = 0; i < n.length; i++) {
            c[i+1] = c[i] + n[i];
            total += n[i];
        }
    }

    public int spin() {
        double r = random.nextDouble() * total;
        
        int a = 0;
        int b = c.length - 1;
        while (b - a > 1) {
            int mid = (a + b) / 2;
            if (c[mid] > r) { 
            	b = mid;
            }else {
            	a = mid;
            }
        }
        return a;
    }
    
    public void updateWeights (double w, int i) {
    	c[i+1] = c[i+1] + w;
    	total += w;
    }
    public double[] getC() {
    	return c;
    }
}