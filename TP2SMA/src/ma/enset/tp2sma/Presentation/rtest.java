package ma.enset.tp2sma.Presentation;

public class rtest {
    public static void main(String[] args) {
        String str = "lol Name : Hp | Type : Gaming | Price : 1212$";
        int start = str.indexOf("Name : ") + "Name : ".length();
        int end = str.indexOf(" |", start);
        String hp = str.substring(start, end);
        System.out.println(hp);
    }
}
