
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import com.password_checker.*;

class PANRequiredException extends Exception {
	String message;

	PANRequiredException(String message) {
		this.message = message;
	}
}

class InvalidAadhaarException extends Exception {
	String message;

	InvalidAadhaarException(String message) {
		this.message = message;
	}
}

class MinBalRequiredException extends Exception {
	String message;

	MinBalRequiredException(String message) {
		this.message = message;
	}
}

class NotEnoughMoneyInAccountException extends Exception {
	String message;

	NotEnoughMoneyInAccountException(String message) {
		this.message = message;
	}
}

class PANFormatMismatchException extends Exception {
	String message;

	PANFormatMismatchException(String message) {
		this.message = message;
	}
}

class BranchNotFoundException extends Exception {
	String message;

	BranchNotFoundException(String message) {
		this.message = message;
	}
}

interface methods {
	void deposit(String name, long id) throws PANRequiredException;

	void withdraw(String name, long id) throws MinBalRequiredException, NotEnoughMoneyInAccountException;

	void check_available_balance(String name, long acc_num);

	public int search(String name, long acc_num);
}

class Account implements Comparable<Account> {
	private String name;
	private long acc_num;
	private String password;
	private float balance;
	private String a_no;
	private String p_no = "None";
	private String branch;
	private String date;

	Account() {
		balance = 0f;
	}

	Account(String name, long acc_num, String password, String branch, float balance, String a_no, String date) {
		this.setName(name);
		this.setAcc_num(acc_num);
		this.setBalance(balance);
		this.setA_no(a_no);
		this.setPassword(password);
		this.setBranch(branch);
		this.setDate(date);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setAcc_num(long acc_num) {
		this.acc_num = acc_num;
	}

	public long getAcc_num() {
		return acc_num;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public float getBalance() {
		return balance;
	}

	public void setA_no(String a_no) {
		this.a_no = a_no;
	}

	public String getA_no() {
		return this.a_no;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return this.date;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBranch() {
		return branch;
	}

	public void setPan(String pan) {
		this.p_no = pan;
	}

	public String getPan() {
		return this.p_no;
	}

	public int compareTo(Account acc) {
		if (this.name.compareTo(acc.name) > 0) {
			return 1;
		} else if (this.name.compareTo(acc.name) < 0) {
			return -1;
		}
		return 0;
	}

	public String toString() {
		return name + "," + acc_num + "," + password + "," + branch + "," + balance + "," + a_no + "," + date;
	}
}

class bank_methods extends Account implements methods {
	public ArrayList<Account> Acc = new ArrayList<Account>();

	void read_object() {
		try {
			File file = new File("info.txt");
			if (file.length() != 0) {
				Scanner sc = new Scanner(file);
				String line;
				while (sc.hasNextLine()) {
					line = sc.nextLine();
					String[] parts = line.split(",");
					Account obj = new Account(parts[0], Long.parseLong(parts[1]), parts[2], parts[3],
							Float.parseFloat(parts[4]), parts[5], parts[6]);
					Acc.add(obj);
				}
			} else {
				System.out.println("info.txt file is empty!!");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	void write_content() throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream("info.txt");
				PrintWriter pw = new PrintWriter(fos)) {
			for (Account object : Acc) {
				pw.println(object.toString() + " ");
			}
		}
	}

	void create(String name, long acc_num)
			throws BranchNotFoundException, InvalidPasswordException, InvalidAadhaarException {
		Scanner sc = new Scanner(System.in);
		int ind = search(name, acc_num);
		if (ind == -1) {

			System.out.println("New Customer");

			System.out.print("\nenter a password :");
			String pass = sc.next();

			while (true) {

				try {
					pass_strength.check_pass(pass);
					break;
				} catch (InvalidPasswordException e) {
					System.out.println(e.getClass().getSimpleName());
					System.out.println(e.message);
					System.out.println(
							"Password's length should be minimum 8.\nIt should contain atleast one uppercase, one lowercase ,one digit and one special character-----------------------");
					System.out.print("\nenter password again :");
					pass = sc.next();

				}
			}
			System.out.println("Available branches are Adyar,Chennai, Delhi,Avadi,Mumbai----------------");
			System.out.print("\nenter branch:");

			String branch = sc.next();
			while (true) {

				try {
					this.checkBranch(branch);
					break;
				} catch (BranchNotFoundException e) {
					System.out.println(e.message);
					System.out.print("\nenter branch again :");
					branch = sc.next();

				}
			}
			System.out.print("\nenter balance amount : ");
			float balance = Float.parseFloat(sc.next());
			System.out.print("\nenter aadhaar number:");
			String a_no;
			a_no = sc.next();
			while (true) {
				try {
					this.Aadhar_check(a_no);
					break;
				} catch (InvalidAadhaarException e) {
					System.out.println(e.message + "\nenter aadhaar number again: ");
					a_no = sc.next();
				}
			}

			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String cur = formatter.format(date);
			System.out.println("Account successfully created on " + cur);
			Acc.add(new Account(name, acc_num, pass, branch, balance, a_no, cur));
		} else {
			System.out
					.println("Account already exists!!\n\nWelcome " + name
							+ "\n\nPlease enter your login credentials!!!");
			login(name, acc_num);

		}

	}

	void login(String username, long acc_num) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("\nenter password:");
			String pass = sc.next();
			String captcha = generate_captcha();
			System.out.println("\nenter the below captcha:\n" + captcha + "\n");
			String cap = sc.next();
			int ind = this.search(username, acc_num);
			if (ind != -1) {
				if (cap.equals(captcha) && username.equals(Acc.get(ind).getName())
						&& pass.equals(Acc.get(ind).getPassword())) {
					System.out.println("Welcome " + Acc.get(ind).getName() + "\nSuccessfully logged in !!");
					break;
				} else {
					System.out.println("Incorrect credentials!! or wrong captcha");
				}
			} else {
				System.out.println("Incorrect credentials!! or wrong captcha");
			}

		}

	}

	public int search(String name, long acc_num) {
		for (Account obj : Acc) {
			if (obj.getAcc_num() == acc_num && obj.getName().equals(name)) {
				return Acc.indexOf(obj);
			}
		}
		return -1;
	}

	void Aadhar_check(String a_no) throws InvalidAadhaarException {
		if (a_no.length() < 12) {
			throw new InvalidAadhaarException("Invalid aadhaar number..");
		}
	}

	String generate_captcha() {
		char alp[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z' };
		String str = "";
		String n;
		Random rand = new Random();
		for (int i = 0; i < 5; i++) {
			if (rand.nextInt(2) == 0) {
				n = Integer.toString(rand.nextInt(10));
				str += n;
			} else {
				str += alp[rand.nextInt(26)];
			}
		}
		return str;
	}

	public void deposit(String name, long acc_num) throws PANRequiredException {
		Scanner sc = new Scanner(System.in);
		int ind = search(name, acc_num);
		Account obj = this.Acc.get(ind);
		System.out.println("enter amount to deposit:");
		float amt = Float.parseFloat(sc.next());
		if (amt > 2000f && obj.getPan().equals("None")) {
			throw new PANRequiredException("You need PAN card"); // check extra condition p_no=="NONE"

		} else {
			obj.setBalance(obj.getBalance() + amt);
			this.Acc.set(ind, obj);
		}

	}

	public void withdraw(String name, long acc_num) throws MinBalRequiredException, NotEnoughMoneyInAccountException {
		Scanner sc = new Scanner(System.in);
		int ind = search(name, acc_num);
		Account obj = this.Acc.get(ind);
		System.out.println("enter amount to withdraw:");
		float withdraw = Float.parseFloat(sc.next());
		if (withdraw < 500f) {
			throw new MinBalRequiredException("Can't withdraw less than 500rs");
		} else if (withdraw > obj.getBalance() || obj.getBalance() == 0) {
			throw new NotEnoughMoneyInAccountException("Insufficient fund");
		} else {
			obj.setBalance(obj.getBalance() - withdraw);
			this.Acc.set(ind, obj);

		}

	}

	void checkBranch(String branch) throws BranchNotFoundException {
		String Branch[] = { "Adyar", "Chennai", "Delhi", "Avadi", "Mumbai" };
		boolean found = false;
		for (int i = 0; i < Branch.length; i++) {
			if (Branch[i].equalsIgnoreCase(branch)) {
				found = true;
				break;
			}
		}
		if (found != true) {
			throw new BranchNotFoundException("Invalid Branch");
		}

	}

	void checkPan(String Pan) throws PANFormatMismatchException {
		char ele;

		if (Pan.length() != 10) {
			throw new PANFormatMismatchException("invalid pan format");
		}
		for (int i = 0; i < 5; i++) {
			ele = Pan.charAt(i);
			if (Character.isLetter(ele) != true) {
				throw new PANFormatMismatchException("invalid pan format");
			}
		}
		for (int i = 5; i < 9; i++) {
			ele = Pan.charAt(i);
			if (Character.isDigit(ele) != true) {
				throw new PANFormatMismatchException("invalid pan format");
			}
		}
		ele = Pan.charAt(9);
		if (Character.isLetter(ele) != true) {
			throw new PANFormatMismatchException("invalid pan format");
		}

	}

	public void check_available_balance(String name, long acc_num) {
		int ind = search(name, acc_num);
		Account obj = this.Acc.get(ind);
		System.out.println("Available balance = " + obj.getBalance());
	}

	void display() {
		Collections.sort(Acc);
		for (Account arr : Acc) {
			System.out.println(arr.toString());
		}
	}

	void transfer_money(String name, long acc_num) throws BranchNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Whom do you want to transfer to ?");
		System.out.println("enter username :");
		String user2 = sc.next();
		System.out.println("enter account number :");
		long acc_num2 = Long.parseLong(sc.next());
		System.out.print("enter branch:");
		String branch = sc.next();
		int ind1 = search(name, acc_num);
		while (true) {

			try {
				this.checkBranch(branch);
				break;
			} catch (BranchNotFoundException e) {
				System.out.println(e.message);
				System.out.print("enter branch again :");
				branch = sc.next();

			}
		}
		int ind = this.search(user2, acc_num2);
		if (ind != -1 && ind1 != -1) {
			System.out.println("enter the amount here : ");
			float amt = Float.parseFloat(sc.next());
			Account obj = this.Acc.get(ind);
			Account sender = this.Acc.get(ind1);
			obj.setBalance(obj.getBalance() + amt);
			sender.setBalance(sender.getBalance() - amt);
			this.Acc.set(ind, obj);
			this.Acc.set(ind1, sender);
			System.out.println("Money transferred successfully !!!");
			System.out.println("Deducted money " + amt + " from your account !!!");
		} else {
			System.out.println("ACCOUNT NOT FOUND!!!");
		}

	}

}

public class banking {

	public static void main(String[] args) throws PANRequiredException, MinBalRequiredException,
			PANFormatMismatchException, NotEnoughMoneyInAccountException, BranchNotFoundException,
			InvalidPasswordException, ClassNotFoundException, IOException, InvalidAadhaarException {

		Scanner S = new Scanner(System.in);
		int op;
		System.out.println(
				"------------------------------------------------WELCOME TO BANKING MANAGEMENT-------------------------------------------\n");
		Account obj;
		int ind = -1;
		String user = "";
		String pan = "";
		long id = -1;
		bank_methods main_acc = new bank_methods();
		FD_Methods FD_acc = new FD_Methods();
		main_acc.read_object();
		FD_acc.read_object_FD();
		char ch = 'y';
		while (ch == 'y') {
			System.out.println("PRESS\na.Basic Account\t\tb.FD Account");
			if (S.next().charAt(0) == 'a') {
				System.out.println(
						"\n\nBASIC /  MAIN ACCOUNT----------------------------------------------------------------------------------------------\nenter name:");
				user = S.next();
				System.out.print("\nenter account number : ");
				id = Long.parseLong(S.next());
				main_acc.create(user, id);
				// main_acc.display();
				while (true) {
					System.out.println(
							" \nPress\n\n1.Deposit\t2.Withdraw\t3.Check Balance\t4.Money Transfer\t5.EXIT");
					op = Integer.parseInt(S.next());
					switch (op) {
						case 1: {
							try {
								main_acc.deposit(user, id);

							} catch (PANRequiredException e) {
								System.out.println(e.message + "\nenter pan number :");
								System.out.println(
										"Pan format is : first 5 characters-> alphabets , next 4 characters -> digits , then one more alphabet-------------------");
								while (true) {
									try {
										pan = S.next();
										main_acc.checkPan(pan);
										System.out.println("enter amount again :");
										float amt = Float.parseFloat(S.next());
										ind = main_acc.search(user, id);
										obj = main_acc.Acc.get(ind);
										obj.setPan(pan);
										obj.setBalance(obj.getBalance() + amt);
										main_acc.Acc.set(ind, obj);
										break;
									} catch (PANFormatMismatchException p) {
										System.out.println("Invalid PAN format!!!");
										System.out.println("enter pan number:");

									}
								}

							}
						}

							break;
						case 2: {
							while (true) {
								try {
									main_acc.withdraw(user, id);
									break;
								} catch (MinBalRequiredException e) {
									System.out.println(e.message);
								} catch (NotEnoughMoneyInAccountException e) {
									System.out.println(e.message);
								}

							}

						}
							break;
						case 3: {
							main_acc.check_available_balance(user, id);
						}
							break;
						case 4: {
							main_acc.transfer_money(user, id);
						}
						default:
							System.out.println("Have a Nice Day!!!");
							break;
					}
					if (op == 5) {
						main_acc.write_content();
						System.out.println("Changes were made in the file!!!");
						break;
					}
				}

			} else {
				System.out.println(
						"\n\nFD ACCOUNT ------------------------------------------------------------------------------------------------------\nenter name:");
				user = S.next();
				System.out.print("Enter FD number : ");
				id = Long.parseLong(S.next());
				FD_acc.create_FD(user, id);
				while (true) {
					System.out.println(
							" \nPress\n1.DISPLAY ACCOUNT DETAILS\t2.PREMATURE CLOSURE\t3.Turn Auto Renewal On or Off\t4.EXIT ");
					op = Integer.parseInt(S.next());
					switch (op) {
						case 1: {
							FD_acc.display_account(user, id);
						}
							break;
						case 2: {
							FD_acc.premature_closure(user, id);
						}
							break;
						case 3: {
							FD_acc.auto_renewal_feature(user, id);
						}
							break;
						default:
							System.out.println("Have a nice day!!!");
							break;
					}
					if (op == 4) {
						FD_acc.write_content();
						System.out.println("Changes were made in the file!!!");
						break;
					}
				}

			}
			System.out.println("Do you want to enter more choices ? (y/n)");
			ch = S.next().charAt(0);

		}

	}

}
