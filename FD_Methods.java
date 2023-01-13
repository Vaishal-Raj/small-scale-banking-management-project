
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

class FixedDeposit {

    private long fdNumber;
    private String name;
    private float fdAmount;
    private int term;
    private float rateOfInterest;
    private String date;
    private boolean auto_renew;

    public FixedDeposit() {
    }

    public FixedDeposit(String name, float fdAmount, long fdNumber, float rateOfInterest, int term, String date,
            boolean value) {
        this.setName(name);
        this.setFdNumber(fdNumber);
        this.setFdAmount(fdAmount);
        this.setTerm(term);
        this.setRateOfInterest(term);
        this.setDate(date);
        this.setAuto(value);
    }

    public void setAuto(boolean value) {
        this.auto_renew = value;
    }

    public boolean getAuto() {
        return this.auto_renew;
    }

    public long getFdNumber() {
        return fdNumber;
    }

    public void setFdNumber(long fdNumber) {
        this.fdNumber = fdNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFdAmount() {
        return fdAmount;
    }

    public void setFdAmount(float fdAmount) {
        this.fdAmount = fdAmount;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getTerm() {
        return this.term;
    }

    public float getRateOfInterest() {
        return rateOfInterest;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setRateOfInterest(int term) {
        float IR;
        if (term >= 12 && term < 24) {
            IR = 4.5f;
        } else if (term >= 24 && term < 36) {
            IR = 5f;
        } else if (term >= 36 && term < 48) {
            IR = 5.5f;
        } else if (term >= 48 && term < 60) {
            IR = 6f;
        } else {
            IR = 7.5f;
        }
        this.rateOfInterest = IR;
    }

    // method to calculate maturity amount
    public float calculateMaturityAmount(int months) {
        float maturityAmount = fdAmount;
        float yr = months / 12;
        maturityAmount += maturityAmount * yr * this.rateOfInterest / 100;
        return maturityAmount;
    }

    public float calculateMaturityAmount() {
        float maturityAmount = fdAmount;
        float yr = this.term / 12;
        maturityAmount += maturityAmount * yr * this.rateOfInterest / 100;
        return maturityAmount;
    }

    public String toString() {
        return name + "," + this.fdAmount + "," + fdNumber + "," + rateOfInterest + "," + term + "," + date + ","
                + String.valueOf(this.auto_renew);
    }

}

public class FD_Methods {
    ArrayList<FixedDeposit> saving = new ArrayList<FixedDeposit>();

    public void show_estimate(float fdAmount, int term, float rateOfInterest) {
        float maturityAmount = fdAmount;
        float yr = term / 12;
        maturityAmount += maturityAmount * yr * rateOfInterest / 100;
        System.out.println("Estimated maturity amount =" + maturityAmount);
    }

    void read_object_FD() {
        try {
            File file = new File("FD_INFO.txt");
            if (file.length() == 0) {
                System.out.println("FD_INFO.txt file is empty!!!");
            } else {
                Scanner sc = new Scanner(file);
                String line;
                while (sc.hasNextLine()) {
                    line = sc.nextLine();
                    String[] parts = line.split(",");
                    if (parts[6].equals("true")) {
                        parts[6] = "True";
                    }
                    FixedDeposit obj = new FixedDeposit(parts[0], Float.parseFloat(parts[1]), Long.parseLong(parts[2]),
                            Float.parseFloat(parts[3]),
                            Integer.parseInt(parts[4]), parts[5], Boolean.parseBoolean(parts[6]));
                    saving.add(obj);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void write_content() throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream("FD_INFO.txt");
                PrintWriter pw = new PrintWriter(fos)) {
            for (FixedDeposit object : saving) {
                pw.println(object.toString() + " ");
            }
        }
    }

    void create_FD(String name, long fd_num) {

        Scanner sc = new Scanner(System.in);
        int ind = search_FD(name, fd_num);
        if (ind == -1) {

            System.out.println("New Customer");
            System.out.print("enter initial fd amount : ");
            float balance = Float.parseFloat(sc.next());
            System.out.println("enter the tenure period(in months, starting term is 12 months ) :");
            int term = Integer.parseInt(sc.next());
            float IR;
            while (true) {

                if (term >= 12 && term < 24) {
                    IR = 4.5f;
                } else if (term >= 24 && term < 36) {
                    IR = 5f;
                } else if (term >= 36 && term < 48) {
                    IR = 5.5f;
                } else if (term >= 48 && term < 60) {
                    IR = 6f;
                } else {
                    IR = 7.5f;
                }
                this.show_estimate(balance, term, IR);
                System.out.println("Do you want to change the tenure period ?");
                if (sc.next().charAt(0) == 'n') {
                    break;
                } else {
                    System.out.println("enter the terms(in months, starting term is 12 months ) :");
                    term = Integer.parseInt(sc.next());
                }

            }
            System.out.println("Do you want to turn on auto renewal of FD account?(y/n)");
            char ch = sc.next().charAt(0);
            boolean value;
            if (ch == 'y') {
                value = true;
            } else {
                value = false;
            }
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String cur = formatter.format(date);
            System.out.println("Account successfully created on " + cur);
            saving.add(new FixedDeposit(name, balance, fd_num, IR, term, cur, value));
        } else {
            System.out.println("Account already exists. \nWelcome " + name);

        }
    }

    void auto_renewal_feature(String name, long acc_num) {
        Scanner sc = new Scanner(System.in);
        int ind = search_FD(name, acc_num);
        int ch;
        FixedDeposit obj = saving.get(ind);
        System.out.println("Do you want to turn on or turn off auto renewal feature ?\n1.On\t2.Off");
        ch = Integer.parseInt(sc.next());
        if (ch == 1) {
            if (!obj.getAuto()) {
                obj.setAuto(true);
                System.out.println("This feature has been turned on!!");
            } else {
                System.out.println("This feature is already enabled by you!!");
            }
        } else {
            if (obj.getAuto() == false) {
                System.out.println("This feature is already disabled!!");
            } else {
                obj.setAuto(false);
                System.out.println("This feature has been turned off!!");
            }
        }
        saving.set(ind, obj);

    }

    public void premature_closure(String name, long acc_num) {
        Scanner sc = new Scanner(System.in);
        int ind = search_FD(name, acc_num);
        FixedDeposit obj = saving.get(ind);
        System.out.println("enter the number of months passed : ");
        int yr = Integer.parseInt(sc.next());
        if (yr < obj.getTerm()) {
            System.out.printf("Maturity amount earned = "
                    + (float) Math.abs(obj.getFdAmount() - obj.calculateMaturityAmount(yr)) + "\nInterest Rate = "
                    + obj.getRateOfInterest());
            System.out.println(
                    "\nDo you want to force close this account ?\nInterest rate will change accordingly with months passed !!(press y/n):");
            if (sc.next().charAt(0) == 'y') {
                obj.setTerm(yr);
                obj.setRateOfInterest(yr);
                obj.setFdAmount(obj.calculateMaturityAmount(yr));
                saving.set(ind, obj);
                System.out.println("Amount withdrawn = " + obj.getFdAmount() + "\nFD Account closed!!!");
                saving.remove(obj);
            }
        } else {
            System.out.println(obj.getAuto());
            if (obj.getAuto() && yr > obj.getTerm()) {
                int new_term = obj.getTerm() * 2;
                obj.setTerm(new_term);
                saving.set(ind, obj);
                System.out.println("You need to wait for another " + (new_term - yr)
                        + "months to attain maturity");
                System.out.println("Do you want to close permaturely?");
                if (sc.next().charAt(0) == 'y') {
                    obj.setFdAmount(obj.calculateMaturityAmount(yr));
                    System.out.println("Amount withdrawn = " + obj.getFdAmount());
                    saving.remove(ind);
                    System.out.println("Your FD account got removed from this bank!!!");
                } else {
                    System.out.println("You can see other options now!!");
                }
            } else {
                System.out.printf("You have completed the term conditions !!!");
                System.out.println("Do you want to extend the FD tenure period ?(y/n)");
                if (sc.next().charAt(0) == 'y') {
                    int new_term = 2 * (obj.getTerm());
                    System.out.println(
                            "New tenure period = " + new_term + "\nYou need to wait for another " + (new_term - yr)
                                    + "months");
                    obj.setTerm(new_term);
                    saving.set(ind, obj);

                } else {
                    obj.setFdAmount(obj.calculateMaturityAmount(yr));
                    System.out.println("Amount withdrawn = " + obj.getFdAmount());
                    saving.remove(ind);
                    System.out.println("Your FD account got removed from this bank!!!");
                }
            }

        }
    }

    void display_account(String name, long acc_num) {
        Scanner sc = new Scanner(System.in);
        int ind = search_FD(name, acc_num);
        if (ind != -1) {
            FixedDeposit obj = saving.get(ind);
            System.out.println("Annual Interest Rate = " + obj.getRateOfInterest() + "\nTerms = " + obj.getTerm()
                    + "\nESTIMATED FD Balance = " + obj.calculateMaturityAmount() + "\nCreation date = " + obj.getDate()
                    + "\nAuto-Renew = " + obj.getAuto());
            System.out.println("enter the number of months passed : ");
            int yr = Integer.parseInt(sc.next());
            if (yr < obj.getTerm()) {
                System.out.println(
                        "Maturity amount earned = "
                                + (float) Math.abs(obj.getFdAmount() - obj.calculateMaturityAmount(yr))
                                + "\nInterest Rate = "
                                + obj.getRateOfInterest() + "%");
            } else if (yr >= obj.getTerm() && obj.getAuto()) {
                System.out.println("Your FD account has been renewed !!");
                System.out.println("The tenure period has been extended!\nNew tenure period = " + (2 * obj.getTerm()));
                System.out.println("You need wait for another " + (2 * obj.getTerm() - yr) + " months"
                        + " Interest rate = " + obj.getRateOfInterest());
            } else {
                System.out.println(
                        "You have completed the tenure period!!\nAmount withdrawn = " + obj.calculateMaturityAmount());
                System.out.println("Do you want to close the account ? (y/n)");
                if (sc.next().charAt(0) == 'y') {
                    System.out.println("Account has been closed!!!");
                    saving.remove(ind);
                } else {
                    System.out.println("Kindly claim at the earliest!!");
                }

            }
        } else {
            System.out.println("Not found!!");
        }

    }

    public int search_FD(String name, long acc_num) {
        for (FixedDeposit obj : saving) {
            if (obj.getFdNumber() == acc_num && obj.getName().equals(name)) {
                return saving.indexOf(obj);
            }
        }
        return -1;
    }

}
