public class Text1 {
    public static void main(String[] args){
        int x = 1;
        while(x <= 5){
            int j = 1;
            while(j <= x){
                System.out.print("*");
                j = j + 1;
            }
            System.out.println();
            x = x + 1;
        }
    }
}