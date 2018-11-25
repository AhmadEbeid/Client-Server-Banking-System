package serverBank;

class SignIn {
	long bankAccountID;
	String password;
}

class SignUp{
	String name;
	String password;
	double amount;
}

class Transfer{
	String otherBankAccountID;
	double amount;
}

class TransferOut{
	String otherBankAccountID;
	String currentBankAccountID;
	double amount;
}

class Deposit{
	double amount;
}


class Withdraw{
	double amount;
}


