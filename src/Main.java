//Judy Li
import java.io.*;
import java.util.*;
import java.math.*;
import java.util.concurrent.ThreadLocalRandom;

class Main {
  public static void main(String[] args) {
    BigInteger x = BigInteger.ZERO;
    BigInteger y1 = BigInteger.ZERO;
    BigInteger y2 = BigInteger.ZERO;
    BigInteger y3 = BigInteger.ZERO;

    List<BigInteger> list = new ArrayList<BigInteger>();
    try{
      Scanner read = new Scanner(new File("src/input.txt"));
      x = read.nextBigInteger(); 
      while(read.hasNext()) {
        list.add(read.nextBigInteger());
      }
    } catch(FileNotFoundException fnf) {
      System.out.println("File was not found.");
    }
    //random numbers, take them out, store in tempList 
    boolean found = false;
    while (!found) {
      int random = ThreadLocalRandom.current().nextInt(0, list.size());
      y1 = list.get(random);
      list.remove(random);
      random = ThreadLocalRandom.current().nextInt(0, list.size());
      y2 = list.get(random);
      list.remove(random);
      random = ThreadLocalRandom.current().nextInt(0, list.size());
      y3 = list.get(random);
      list.remove(random);
      //see if gcd | x. (x % gcd == 0)
      BigInteger temp = gcdCalculator(gcdCalculator(y1, y2), y3);
      if (x.mod(temp).compareTo(BigInteger.ZERO) == 0) {
        found = true;
      } else {
        list.add(y1);
        list.add(y2);
        list.add(y3);
      }
    }
    //gcd of first 2 numbers
    BigInteger gcd1 = gcdCalculator(y1, y2);
    BigInteger [] solution = findTwoUnknowns(y3, gcd1);
    BigInteger z = x.multiply(solution[0]);
    BigInteger nota = x.multiply(solution[1]);
    //x*solution[1] = t 
    //r1x + r2y = t
    BigInteger gcd = gcdCalculator(y1, y2);
    solution = findTwoUnknowns(y1.divide(gcd), y2.divide(gcd));

    try{
      PrintWriter writer = new PrintWriter("src/output.txt");
      writer.println(y1 + "  " + nota.multiply(solution[0]));
      writer.println(y2 + "  " + nota.multiply(solution[1]));
      writer.println(y3 + "  " + z);
      writer.close();
    } catch(FileNotFoundException fnf){
      System.out.println("File was not found.");
    }
  }

  public static BigInteger[] findTwoUnknowns(BigInteger y1, BigInteger y2) {
    BigInteger[] arr;
    //base case
    if (y2.compareTo(BigInteger.ZERO) == 0){
      arr = new BigInteger[]{BigInteger.ONE, BigInteger.ZERO};
      return arr;
    } 
    else {
      //arr = new int[2];
      arr = findTwoUnknowns(y2, (y1.mod(y2)));
      BigInteger temp = y1.divide(y2).multiply(arr[1]);
      BigInteger[] sols = new BigInteger[]{arr[1], (arr[0].subtract(temp))};
      return sols;
    }
  }

  public static BigInteger gcdCalculator(BigInteger a, BigInteger b) {
    BigInteger bigger, smaller;
    BigInteger divisor;
    BigInteger tempRemainder;
    BigInteger remainder = BigInteger.ONE;
    //find the bigger number
    if (a.compareTo(b) == 0) {
      return a;
      //a < b
    } else if (a.compareTo(b) < 0) {
      if (b.mod(a).compareTo(BigInteger.ZERO) == 0) {
        return a;
      }
      bigger = b;
      smaller = a;
    } else {
      if (a.mod(b).compareTo(BigInteger.ZERO) == 0) {
        return b;
      }
      bigger = a;
      smaller = b;
    }
    
    divisor = bigger.divide(smaller);
    BigInteger temp = smaller.multiply(divisor);
    tempRemainder = bigger.subtract(temp);
    //tempRemainder > 0
    while (tempRemainder.compareTo(BigInteger.ZERO) > 0) {
      bigger = smaller;
      smaller = tempRemainder;
      divisor = bigger.divide(smaller);
      temp = bigger.mod(smaller);
      if (temp.compareTo(BigInteger.ZERO) == 0) {
        return smaller;
      }
      temp = smaller.multiply(divisor);
      tempRemainder = bigger.subtract(temp);
      if (tempRemainder.compareTo(BigInteger.ZERO) != 0) {
        remainder = tempRemainder;
      }
    }
    return remainder;  
  }
}