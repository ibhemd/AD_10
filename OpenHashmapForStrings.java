import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OpenHashmapForStrings {

    Integer m;
    String[] S;

    public OpenHashmapForStrings(Integer m){
        this.m = m;
        this.S = new String[m];
    }

    public Integer insert(String[] T, String k){
        Integer i = 0;
        while (i < this.m) {
            Integer j = hash(k,i);
            //System.out.println(j);
            if (T[j] == null) {
                T[j] = k;
                return j;
            } else {
                i += 1;
            }
        }
        System.out.println("Overflow: " + k);
        return null;
    }

    public Integer search(String[] T, String k){
        Integer i = 0;
        while (i < this.m) {
            int j = hash(k,i);
            if (T[j] == null) {
                return null;
            }
            if (T[j].equals(k)) {
                return j;
            }
            i += 1;
        }
        return null;
    }

    public Integer hash(String k, Integer i){
        char[] chars = k.toCharArray();
        Integer n = 0;
        for (char a : chars) {
            n += (Integer)(int) a;
        }
        //return linearProbing(n,i);
        //return squareProbing(n,i);
        return doubleHashing(n,i);
    }

    public Integer linearProbing(Integer n, Integer i){
        return  ((n%this.m+i)%this.m);
    }

    public Integer squareProbing(Integer n, Integer i){
        Float c_1 = 0.5f;
        Float c_2 = 0.5f;
        return (Integer)(int) ((n%this.m + c_1*i + c_2*(i^2))%this.m);
    }

    public Integer doubleHashing(Integer n, Integer i){
        Integer h1 = n%this.m;
        Integer h2 = (1+(n%(this.m-1)));
        //System.out.println("h1: " + h1 + " || h2: " + h2 + " || m: " + this.m); //63537
        return (h1+i*h2)%this.m;
    }

    public void delete(String[] T, String k){
        Integer j = search(T,k);
        if (j != null) {
            T[j] = "Deleted";
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        OpenHashmapForStrings tt = new OpenHashmapForStrings(200000);
        OpenHashmapForStrings m = new OpenHashmapForStrings(1000000);

        Scanner scan = new Scanner(new File("src/OfficialScrabbleWordListGerman.txt"));
        long begin = System.nanoTime();
        while (scan.hasNext()) {
            tt.insert(tt.S,scan.nextLine());                                              // change table here
        }
        long end = System.nanoTime();
        long velocity = end - begin;
        System.out.println("time to insert: " + velocity);

        scan = new Scanner(new File("src/AreMyFriendsCheating.txt"));
        begin = System.nanoTime();
        int errors = 0;
        while (scan.hasNext()) {
            Integer err = tt.search(tt.S,scan.nextLine());                                 // change table here
            if (err == null) {
                errors += 1;
            }
        }
        end = System.nanoTime();
        velocity = end - begin;
        System.out.println("time to search: " + velocity);
        System.out.println("errors: " + errors);

        /*
        linearProbing:
                    200.000             1.000.000
        insert      514300383600        514784534000
        search      29289738500         31063924500
        errors      1814                1814

        squareProbing:
                    200.000             1.000.000
        insert      563053317900        681833995300
        search      29436026200         28950606300
        errors      1814                1814

        doubleHashing:
                    200.000             1.000.000
        insert      151046828400        6017577500
        search      14286986000         383037400
        errors      1985                1814

            --> weil m und h2(k) beim 'doubleHashing' oft nicht teilerfremd sind, kommt es häufig zum Overflow
                und somit auch zu mehr Fehlern bei 'search'

         */

    }

}
