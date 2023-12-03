package com.se300.ledger.model.complete;

import com.se300.ledger.TestSmartStoreApplication;
import com.se300.ledger.model.Account;
import com.se300.ledger.model.Block;
import com.se300.ledger.model.Transaction;
import com.se300.ledger.service.Ledger;
import com.se300.ledger.service.LedgerException;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.hibernate.mapping.Map;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {TestSmartStoreApplication.class})
public class CompleteTest {
    private Ledger ledger;

    @BeforeEach
    public void setUp() {
        // Initialize a Ledger object for testing
        ledger = Ledger.getInstance("Test Ledger", "Test Description", "Test Seed");
    }

    @Test
    public void testGetSeedAndSetSeed() {
        Ledger ledger = Ledger.getInstance("MyLedger","Test Ledger", "seed123");
        assertEquals("seed123", ledger.getSeed());
        
        ledger.setSeed("newseed456"); 
        assertEquals("newseed456", ledger.getSeed());
    }

    @Test
    public void testCreateAccount() throws LedgerException {
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");
        String address = "account1";
        
        Account account = ledger.createAccount(address);
        
        assertNotNull(account);
        assertEquals(address, account.getAddress());
        assertEquals(0, account.getBalance());
    }

    @Test
    public void testProcessTransaction() throws LedgerException {
    
        // Set up
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");
        
        String payerAddress = "payer";
        Account payer = new Account(payerAddress, 100);
        
        String receiverAddress = "receiver"; 
        Account receiver = new Account(receiverAddress, 0);
        
        ledger.createAccount(payerAddress);
        ledger.createAccount(receiverAddress);
        
        // Test valid transaction 
        int validAmount = 50;
        int validFee = 10;
        String validNote = "Test transaction";
        
        Transaction validTransaction = new Transaction("tx1", validAmount, validFee, 
                validNote, payer, receiver); 
        
        String txId = ledger.processTransaction(validTransaction);
        
        assertNotNull(txId);
        assertEquals(validAmount, ledger.getAccountBalance(receiverAddress));
    }


    @Test  
    public void testGetAccountBalance() throws LedgerException {
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");
        
        // Existing account
        String existAddress = "exist";
        ledger.createAccount(existAddress);
        
        assertEquals(0, ledger.getAccountBalance(existAddress));
        
        // Non existing account 
        try {
            ledger.getAccountBalance("nonexist");
            fail("Should throw exception");
        } catch (LedgerException e) {
        // Expected 
        }
    }



    @Test
    public void testSetAddress() {
        Account account = new Account("initialAddress", 100);
        account.setAddress("newAddress");
        assertEquals("newAddress", account.getAddress());
    }

    @Test
    public void testLedgerExceptionGetAction() {
        LedgerException exception = new LedgerException("TestAction", "TestReason");
        assertEquals("TestAction", exception.getAction());
    }

    @Test
    public void testLedgerExceptionSetAction() {
        LedgerException exception = new LedgerException("TestAction", "TestReason");
        exception.setAction("NewAction");
        assertEquals("NewAction", exception.getAction());
    }

    @Test
    public void testLedgerExceptionGetReason() {
        LedgerException exception = new LedgerException("TestAction", "TestReason");
        assertEquals("TestReason", exception.getReason());
    }

    @Test
    public void testLedgerExceptionSetReason() {
        LedgerException exception = new LedgerException("TestAction", "TestReason");
        exception.setReason("NewReason");
        assertEquals("NewReason", exception.getReason());
    }

    @Test
    public void testSetBlockNumber() {
        Block block = new Block(1, "PreviousHash");
        block.setBlockNumber(2);
        assertEquals(2, block.getBlockNumber());
    }

    @Test
    public void testSetPreviousHash() {
        Block block = new Block(1, "PreviousHash");
        block.setPreviousHash("NewHash");
        assertEquals("NewHash", block.getPreviousHash());
    }

    @Test
    public void testGetPreviousHash() {
        Block block = new Block(1, "PreviousHash");
        assertEquals("PreviousHash", block.getPreviousHash());
    }

    @Test
    public void testGetPreviousBlock() {
        Block block1 = new Block(1, "PreviousHash");
        Block block2 = new Block(2, "PreviousHash");
        block2.setPreviousBlock(block1);
        Block previousBlock = block2.getPreviousBlock();
        assertEquals(block1, previousBlock);
    }

        @Test
    public void testSetTransactionID() {
        Account payer = new Account("payerAddress", 100);
        Account receiver = new Account("receiverAddress", 0);
        Transaction transaction = new Transaction("InitialID", 100, 10, "InitialNote", payer, receiver);

        transaction.setTransactionId("NewID");
        assertEquals("NewID", transaction.getTransactionId());
    }

    @Test
    public void testSetAmount() {
        Account payer = new Account("payerAddress", 100);
        Account receiver = new Account("receiverAddress", 0);
        Transaction transaction = new Transaction("InitialID", 100, 10, "InitialNote", payer, receiver);

        transaction.setAmount(200);
        assertEquals(200, transaction.getAmount());
    }

    @Test
    public void testSetFee() {
        Account payer = new Account("payerAddress", 100);
        Account receiver = new Account("receiverAddress", 0);
        Transaction transaction = new Transaction("InitialID", 100, 10, "InitialNote", payer, receiver);

        transaction.setFee(20);
        assertEquals(20, transaction.getFee());
    }

    @Test
    public void testSetNote() {
        Account payer = new Account("payerAddress", 100);
        Account receiver = new Account("receiverAddress", 0);
        Transaction transaction = new Transaction("InitialID", 100, 10, "InitialNote", payer, receiver);

        transaction.setNote("NewNote");
        assertEquals("NewNote", transaction.getNote());
    }

    @Test
    public void testSetPayer() {
        Account payer1 = new Account("payerAddress1", 100);
        Account payer2 = new Account("payerAddress2", 200);
        Account receiver = new Account("receiverAddress", 0);
        Transaction transaction = new Transaction("InitialID", 100, 10, "InitialNote", payer1, receiver);

        transaction.setPayer(payer2);
        assertEquals(payer2, transaction.getPayer());
    }

    @Test
    public void testSetReceiver() {
        Account payer = new Account("payerAddress", 100);
        Account receiver1 = new Account("receiverAddress1", 0);
        Account receiver2 = new Account("receiverAddress2", 50);
        Transaction transaction = new Transaction("InitialID", 100, 10, "InitialNote", payer, receiver1);

        transaction.setReceiver(receiver2);
        assertEquals(receiver2, transaction.getReceiver());
    }

    @Test
    public void testSetSeedValid() {
        // Set a valid seed
        String newSeed = "NewSeed123";
        ledger.setSeed(newSeed);

        // Check if the seed is set correctly
        assertEquals(newSeed, ledger.getSeed());
    }

    @Test
    public void testSetSeedEmpty() {
        // Set an empty seed
        String newSeed = "";
        ledger.setSeed(newSeed);

        // Check if the seed is set to the empty string
        assertEquals(newSeed, ledger.getSeed());
    }

    @Test
    public void testSetName() {
    // Get the initial name
    String initialName = ledger.getName();

    // Set a new name
    String newName = "New Ledger Name";
    ledger.setName(newName);

    // Check if the name is set correctly
    assertEquals(newName, ledger.getName());

    // Reset the name to its initial value
    ledger.setName(initialName);
    assertEquals(initialName, ledger.getName());
    }

    @Test
    public void testSetDescription() {
    // Get the initial description
    String initialDescription = ledger.getDescription();

    // Set a new description
    String newDescription = "New Ledger Description";
    ledger.setDescription(newDescription);

    // Check if the description is set correctly
    assertEquals(newDescription, ledger.getDescription());

    // Reset the description to its initial value
    ledger.setDescription(initialDescription);
    assertEquals(initialDescription, ledger.getDescription());
    }

    @Test
    public void testProcessTransactionWithInvalidNote() throws LedgerException {
        // Create accounts for testing
        Account senderAccount = ledger.createAccount("senderAddress");
        Account receiverAccount = ledger.createAccount("receiverAddress");

        // Initial balances for sender and receiver
        int initialSenderBalance = senderAccount.getBalance();
        int initialReceiverBalance = receiverAccount.getBalance();

        // Create a test transaction with an extremely long note (exceeding 1024 characters)
        int transactionAmount = 50;
        int transactionFee = 10;
        String transactionNote = generateLongNote();  // Generate a long note
        Transaction transaction = new Transaction("TestTransactionID", transactionAmount, transactionFee, transactionNote, senderAccount, receiverAccount);

        try {
            // Attempt to process the test transaction
            ledger.processTransaction(transaction);

            // If the exception is not thrown, fail the test
            fail("Expected LedgerException with the message: 'Note Length Must Be Less Than 1024 Chars'");
        } catch (LedgerException e) {
            // Ensure that the exception message matches the expected message
            assertEquals("Process Transaction", e.getAction());
            assertEquals("Note Length Must Be Less Than 1024 Chars", e.getReason());

            // Ensure the balances remain unchanged
            assertEquals(initialSenderBalance, senderAccount.getBalance());
            assertEquals(initialReceiverBalance, receiverAccount.getBalance());
        }
    }

    private String generateLongNote() {
        // Generate a very long note with more than 1024 characters
        StringBuilder longNote = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            // Repeat a 5-character segment to make the note longer
            longNote.append("Lorem ");
        }
        return longNote.toString();
    }
}