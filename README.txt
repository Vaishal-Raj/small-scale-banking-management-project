NOTE : Just extract the zip file and execute the banking.java file in the terminal.
        You can delete the two text files if you want but you'll have to create the accounts thorugh the program , so that you can test login.

1)We have used two text files as a kind of database. "info.txt" is for basic account and "FD_INFO.txt" is for FD account

2)The user is asked to whether he wants to create a Basic Account or a FD account.
    Press 'a' for Basic Account and 'b' for FD Account.

3)Go through the on-screen instructions to fill up the user-details like name, account number , balance etc.

4)If the user is an existing user (go through the .txt files from 1) to see the  predefined users) then the user is asked to validate his details by entering his password and also a captcha will be provided.

5)If you are a new user make sure that your password is strong ie.. length should be atleast 8 , atleast one uppercase,lowercase,digit,and a special character or else it will throw an Exception

6)These are the available branches......{ "Adyar", "Chennai", "Delhi", "Avadi", "Mumbai" }. You can't enter any other branches.

7)Then the user will be asked to press 1 for Deposit , 2 for Withdraw , 3 for check balance 4 for money transfer 5 to exit the loop.

8)Deposit function may throw an exception and you will asked to enter a pan number. The format is first five should be alphabets , next four should be digit and then finally another character. Length should be 10.

9)You can't proceed furthur unless you clear a particular exception because they are all used in a while loop.

10)After using EXIT option you can see the changes made in the file. Then you will be asked to whether you want to enter more choices ? press 'y' to execute the loop again ie.. from 2). And 'n' to exit the program.

11)If the user enters 'b' then the user can go through the on-screen instructions to executethe operations made for FD Account.

12)Finally after pressing EXIT option the user can the changes made in the file.

13)The user has to press 'n' finally to exit the program.

