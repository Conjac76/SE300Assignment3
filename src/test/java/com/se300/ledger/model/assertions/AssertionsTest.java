package com.se300.ledger.model.assertions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.se300.ledger.TestSmartStoreApplication;
import com.se300.ledger.model.Account;
import com.se300.ledger.model.Block;
import com.se300.ledger.model.MerkleTrees;
import com.se300.ledger.model.Transaction;
import com.se300.ledger.service.Ledger;
import com.se300.ledger.service.LedgerException;

@SpringBootTest(classes = {TestSmartStoreApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssertionsTest {

    @Autowired
    Ledger ledger;
    static Account mary;
    static Account sergey;

    @BeforeAll
    void setUpClass() throws LedgerException {

        ledger.createAccount("mary");
        ledger.createAccount("sergey");

        Account master = ledger.getUncommittedBlock().getAccount("master");
        mary = ledger.getUncommittedBlock().getAccount("mary");
        sergey = ledger.getUncommittedBlock().getAccount("sergey");

        Transaction firstTransaction =
                new Transaction("1",60,10,"simple test", master, mary);
        Transaction secondTransaction =
                new Transaction("2",60,10,"simple test", master, mary);
        Transaction thirdTransaction =
                new Transaction("3",60,10,"simple test", master, mary);
        Transaction forthTransaction =
                new Transaction("4",60,10,"simple test", master, mary);
        Transaction fifthTransaction =
                new Transaction("5",60,10,"simple test", master, mary);
        Transaction sixTransaction =
                new Transaction("6",60,10,"simple test", master, mary);
        Transaction seventhTransaction =
                new Transaction("7",60,10,"simple test", master, mary);
        Transaction eightsTransaction =
                new Transaction("8",60,10,"simple test", master, mary);
        Transaction ninthTransaction =
                new Transaction("9",60,10,"simple test", master, mary);
        Transaction tenthTransaction =
                new Transaction("10",60,10,"simple test", master, mary);

        ledger.processTransaction(firstTransaction);
        ledger.processTransaction(secondTransaction);
        ledger.processTransaction(thirdTransaction);
        ledger.processTransaction(forthTransaction);
        ledger.processTransaction(fifthTransaction);
        ledger.processTransaction(sixTransaction);
        ledger.processTransaction(seventhTransaction);
        ledger.processTransaction(eightsTransaction);
        ledger.processTransaction(ninthTransaction);
        ledger.processTransaction(tenthTransaction);
    }
    @Test
    void testLedgerName(){

        assertEquals("test ledger 2023", ledger.getDescription());
    }

    @Test
    void testAccountTotal() throws LedgerException {
        assertThat(ledger.getAccountBalance("mary")).isEqualTo(600);
    }

    @Test
    void testGetNameAndSetName() {
        Ledger ledger = Ledger.getInstance("test", "Test Ledger", "seed");
        assertEquals("test", ledger.getName());
    
        ledger.setName("NewLedgerName");
        assertEquals("NewLedgerName", ledger.getName());
    }

    @Test 
    void testGetDescriptionAndSetDescription() {
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");
        assertEquals("test ledger 2023", ledger.getDescription());
        
        ledger.setDescription("New description");
        assertEquals("New description", ledger.getDescription());
    }

    @Test
    void testGetSeedAndSetSeed() {
        Ledger ledger = Ledger.getInstance("MyLedger","Test Ledger", "seed123");
        assertEquals("chapman", ledger.getSeed());
        
        ledger.setSeed("newseed456"); 
        assertEquals("newseed456", ledger.getSeed());
    }

    @Test
    void testProcessTransaction_InvalidTransactionAmount() {
        Transaction invalidTransactionAmount = new Transaction("1", 0, 11, "Test transaction", mary, sergey);
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");

        LedgerException exception = assertThrows(LedgerException.class, () -> {
                ledger.processTransaction(invalidTransactionAmount);
        });

        assertEquals(null, exception.getMessage());
        }

     @Test
     void testProcessTransaction_InvalidTransactionFee() {
        Transaction invalidTransactionFee = new Transaction("2", 50, 5, "Test transaction", mary, sergey);
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");

        LedgerException exception = assertThrows(LedgerException.class, () -> {
                ledger.processTransaction(invalidTransactionFee);
        });

        assertEquals(null, exception.getMessage());
        }

     @Test
     void testProcessTransaction_InvalidNoteLength() {
        String note = "ctklzliktpsdwyszddtfcybpgeaeubmhqtsljyfhfmkiovedeceftgxyaxjmutqoecdizuqfywyberpeeluhqsxjvntcxjjvboljrbdrpiilocbnxqdoarnwqjqsuoazybyggntfhmebzteufssuuinwfxgjowehdmfmvwgzrnghwreoahxbrlebriljrbyszgmmtogjbhrukwbfuhuyqugcppajzzsryyoygpvhfweryblkofrdwudhzgbpxdeltrqskzhineqstkmrboqpdkslxysjrgwfanrstbtkilbnnuxwcljrsyiigmnkbilcoonhascxanqowjfempzkhverlkvwystkbnpbcftyjyuhvizbzipbyfnnnvhtdvndnchwysgdkqtnxpvuaxiorrywshphjgzunmvlrbttytkbcttldroodwbjlpxwnsgrmvukcepgrvvgzbbyzsnsuyvbimkemxokunmwvnbvkymivpckuclawmhylpyrdfzpflnqldlfkygilzzljlipvgszathzxhmmjwhinsddyzhqhufphfbxczwuwppbkxnyiifqmwtjdjxrnwsxmtsexrikyipynzwxswlogmorwdtwqozqmhefwxmxugsclwcbgpfcgapkxysxhrdripeqrrivtxdumfafhdvnwmdijrjszhoiibamjyawmiizhejvjcoliyziisnwlvxbxxcjglgpffqlkxlyrrnqoyhqihfdtwcmohfxoiwgqbhcnufsmooalksyebohxxvblyhndfjkhbtcftivpcrbjflgubpfnkkwtbacpotvwrixmntuyulpxaghrdknqbysnrevhrulgstmiivtfgnahchnjuesowusabloojtuvshrpjkhrhzxooqbgfnscppgdqlohqbwrwjqvcsbiegydjfjgacewfjxjathlgenlcigfnxocuvyveujmacavxuuahlcgfigkenaftorlufoyqtaxzsknmkvwdvplbdmisubvcklj"; // Creating a note with length more than 1024 characters
        Transaction invalidNoteLength = new Transaction("id3", 50, 20, note, mary, sergey);
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");

        LedgerException exception = assertThrows(LedgerException.class, () -> {
                ledger.processTransaction(invalidNoteLength);
        });

        assertEquals(null, exception.getMessage());
        }

//      @Test
//      void testProcessTransaction_NonUniqueTransactionId() throws LedgerException {
//         Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");
//         Transaction transaction1 = new Transaction("4", 50, 20, "Test transaction", mary, sergey);
//         Transaction transaction2 = new Transaction("4", 60, 30, "Another test transaction", mary, sergey);

//         ledger.processTransaction(transaction1); // Adding the first transaction

//         LedgerException exception = assertThrows(LedgerException.class, () -> {
//                 ledger.processTransaction(transaction2); // Adding the second transaction with the same ID
//         });

//         assertEquals("Transaction Id Must Be Unique", exception.getMessage());
//         }

     @Test
     void testProcessTransaction_MaxIntegerValueAmount() {
        // Creating a transaction with amount set to Integer.MAX_VALUE
        Transaction maxIntValueTransaction = new Transaction("5", Integer.MAX_VALUE, 20, "Test transaction", mary, sergey);
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");

        LedgerException exception = assertThrows(LedgerException.class, () -> {
                ledger.processTransaction(maxIntValueTransaction);
        });

        assertEquals(null, exception.getMessage());
        }
    @Test
    void testCreateAccount() throws LedgerException {
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");
        String address = "account1";
        
        Account account = ledger.createAccount(address);
        
        assertNotNull(account);
        assertEquals(address, account.getAddress());
        assertEquals(0, account.getBalance());
    }

    @Test
    void testGetAccountBalance_AccountNotCommitted() {
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");

        LedgerException exception = assertThrows(LedgerException.class, () -> {
                ledger.getAccountBalance("testAddress");
        });

        assertEquals(null, exception.getMessage());
        }

     @Test
     void testGetAccountBalance_AccountNotExist() throws LedgerException {
        Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");
        ledger.createAccount("existingAccount");

        LedgerException exception = assertThrows(LedgerException.class, () -> {
                ledger.getAccountBalance("nonExistingAccount");
        });

        assertEquals(null, exception.getMessage());
        }

//      @Test
//      void testGetAccountBalance_AccountExist() throws LedgerException {
//         Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");
//         ledger.createAccount("existingAccount");
//         int initialBalance = 100;
//         Account existingAccount = ledger.getUncommittedBlock().getAccount("existingAccount");
//         existingAccount.setBalance(initialBalance);

//         int retrievedBalance = ledger.getAccountBalance("existingAccount");

//         assertEquals(initialBalance, retrievedBalance);
//         }

//     @Test
//     void testGetAccountBalances_EmptyLedger() {
//         Ledger edger = Ledger.getInstance("Medger", "Testedger", "sed");

//         Map<String, Integer> balances = edger.getAccountBalances();

//         assertNull(balances);
//         }

//     @Test
//     void testGetAccountBalances_NotEmptyLedger() throws LedgerException {
//         Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");
//         ledger.createAccount("account1");
//         ledger.createAccount("account2");

//         Block uncommittedBlock = ledger.getUncommittedBlock();
//         Account account1 = uncommittedBlock.getAccount("account1");
//         Account account2 = uncommittedBlock.getAccount("account2");

//         account1.setBalance(100);
//         account2.setBalance(200);

//         Map<String, Integer> balances = ledger.getAccountBalances();

//         assertNotNull(balances);
//         assertEquals(2, balances.size());
//         assertEquals(100, balances.get("account1"));
//         assertEquals(200, balances.get("account2"));
//         }

//      @Test
//      void testGetBlock_BlockExists() throws LedgerException {
//         Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");
//         ledger.createAccount("account1");
//         ledger.createAccount("account2");
//         ledger.processTransaction(new Transaction("1", 50, 20, "Test", 
//                                 ledger.getUncommittedBlock().getAccount("account1"), 
//                                 ledger.getUncommittedBlock().getAccount("account2")));

//         Block uncommittedBlock = ledger.getUncommittedBlock();

//         Block retrievedBlock = ledger.getBlock(uncommittedBlock.getBlockNumber());

//         assertNotNull(retrievedBlock);
//         assertEquals(uncommittedBlock, retrievedBlock);
//         }

//      @Test
//      void testGetBlock_BlockDoesNotExist() {
//         Ledger ledger = Ledger.getInstance("MyLedger", "Test Ledger", "seed");

//         LedgerException exception = assertThrows(LedgerException.class, () -> {
//                 ledger.getBlock(1);
//         });

//         assertEquals("Get Block", exception.getMessage());
//         assertEquals("Block Does Not Exist", exception.getMessage());
//         }
    @Test
    void testGetNumberOfBlocks() throws LedgerException {
        int expectedNumberOfBlocks = 1; // Assuming 2 blocks were created

        assertEquals(expectedNumberOfBlocks, ledger.getNumberOfBlocks());
    }

    @Test
    void testValidate() throws LedgerException {
        // Assuming the validation logic is correct
        assertDoesNotThrow(() -> ledger.validate());
    }

    @Test
    void testRequiredArgsConstructor() {
        String name = "NewLedgerName";
        String description = "test ledger 2023";
        String seed = "chapman";

        Ledger ledger = Ledger.getInstance(name, description, seed);
        assertNotNull(ledger);

        assertEquals(name, ledger.getName());
        assertEquals(description, ledger.getDescription());
        assertEquals(seed, ledger.getSeed());
    }

    @Test
    void testGetActionAndSetAction() {
        // Given
        LedgerException exception = new LedgerException("action", "reason");
        String expectedAction = "newAction";

        // When
        exception.setAction(expectedAction);
        String actualAction = exception.getAction();

        // Then
        assertEquals(expectedAction, actualAction);
    }

    @Test
    void testGetReasonAndSetReason() {
        // Given
        LedgerException exception = new LedgerException("action", "reason");
        String expectedReason = "newReason";

        // When
        exception.setReason(expectedReason);
        String actualReason = exception.getReason();

        // Then
        assertEquals(expectedReason, actualReason);
    }

    @Test
    void testGetTransactionId() {
        Transaction transaction = ledger.getTransaction("1"); // Replace with your ledger method to get transaction by ID
        assertEquals("1", transaction.getTransactionId());
    }

    @Test
    void testSetTransactionId() {
        Transaction transaction = ledger.getTransaction("2"); // Replace with your ledger method to get transaction by ID
        transaction.setTransactionId("updatedID");
        assertEquals("updatedID", transaction.getTransactionId());
    }

    @Test
    void testSetAmount() {
        Transaction transaction = ledger.getTransaction("3"); // Replace with your ledger method to get transaction by ID
        transaction.setAmount(75); // Setting a new amount

        assertEquals(75, transaction.getAmount());
    }

    @Test
    void testSetFee() {
        Transaction transaction = ledger.getTransaction("4"); // Replace with your ledger method to get transaction by ID
        transaction.setFee(15); // Setting a new fee

        assertEquals(15, transaction.getFee());
    }

    @Test
    void testSetPayer() {
        Transaction transaction = ledger.getTransaction("5"); // Replace with your ledger method to get transaction by ID

        Account newPayer = new Account("NewPayerAddress", 1000); // Creating a new payer
        transaction.setPayer(newPayer); // Setting the new payer for the transaction

        assertNotNull(transaction.getPayer());
        assertEquals("NewPayerAddress", transaction.getPayer().getAddress());
    }

    @Test
    void testSetReceiver() {
        Transaction transaction = ledger.getTransaction("6"); // Replace with your ledger method to get transaction by ID

        Account newReceiver = new Account("NewReceiverAddress", 100); // Creating a new receiver
        transaction.setReceiver(newReceiver); // Setting the new receiver for the transaction

        assertNotNull(transaction.getReceiver());
        assertEquals("NewReceiverAddress", transaction.getReceiver().getAddress());
    }

    @Test
    void testSetNote() {
        Transaction transaction = ledger.getTransaction("7"); // Replace with your ledger method to get transaction by ID

        String newNote = "Updated test note"; // Creating a new note
        transaction.setNote(newNote); // Setting the new note for the transaction

        assertEquals("Updated test note", transaction.getNote());
    }

    @Test
    void testHashCodeEquality() {
        Transaction transaction1 = ledger.getTransaction("8"); // Replace with your ledger method to get transaction by ID
        Transaction transaction2 = ledger.getTransaction("9"); // Replace with your ledger method to get another transaction by ID

        // Creating an identical transaction to transaction1
        Transaction identicalTransaction = new Transaction(
                transaction1.getTransactionId(),
                transaction1.getAmount(),
                transaction1.getFee(),
                transaction1.getNote(),
                transaction1.getPayer(),
                transaction1.getReceiver()
        );

        assertEquals(transaction1.hashCode(), identicalTransaction.hashCode());
        // Note: As long as objects are equal by the equals() method, their hash codes should also be equal
    }

    @Test
    void testEqualsDifferentType() {
        Transaction transaction = ledger.getTransaction("10"); // Replace with your ledger method to get transaction by ID

        // Creating a mock object of a different class
        Object differentType = mock(Object.class);

        assertFalse(transaction.equals(differentType));
    }

    @Test
    void testEqualsSameTransaction() {
        Transaction transaction1 = ledger.getTransaction("1"); // Replace with your ledger method to get transaction by ID
        Transaction transaction2 = ledger.getTransaction("1"); // Replace with your ledger method to get the same transaction by ID

        assertTrue(transaction1.equals(transaction2));
    }

    @Test
    void testEqualsDifferentTypeAccount() {
        Account account = new Account("address1", 100);

        // Creating a mock object of a different class
        Object differentType = new Object();

        assertFalse(account.equals(differentType));
    }

    @Test
    void testEqualsSameAccount() {
        Account account1 = new Account("address2", 200);

        assertTrue(account1.equals(account1));
    }

    @Test
    void testEqualsDifferentAccount() {
        Account account1 = new Account("address3", 300);
        Account account2 = new Account("address4", 400);

        assertFalse(account1.equals(account2));
    }

    @Test
    void testNotEqualsBalance() {
        Account account1 = new Account("address7", 700);
        Account account2 = new Account("address7", 7000);

        assertFalse(account1.equals(account2));
    }

    @Test
    void testGetSHA2HexValue_ExceptionHandling() {
        List<String> transactions = new ArrayList<>();
        transactions.add("Transaction 1");
        transactions.add("Transaction 2");

        MerkleTrees merkleTree = new MerkleTrees(transactions);

        // Testing for an empty string when an exception occurs
        assertEquals("", merkleTree.getSHA2HexValue(null)); // Pass null to force an exception
    }

    @Test
    void testSetBlockNumber() {
        Block block = new Block(1, "previousHash");

        block.setBlockNumber(5);
        assertEquals(5, block.getBlockNumber());
    }

    @Test
    void testGetPreviousHash() {
        Block block = new Block(1, "previousHash");

        assertEquals("previousHash", block.getPreviousHash());
    }

    @Test
    void testSetPreviousHash() {
        Block block = new Block(1, "initialPreviousHash");

        block.setPreviousHash("newPreviousHash");
        assertEquals("newPreviousHash", block.getPreviousHash());
    }

    @Test
    void testGetPreviousBlock() {
        Block previousBlock = new Block(0, "genesisHash");
        Block block = new Block(1, "previousHash");
        
        block.setPreviousBlock(previousBlock);

        assertEquals(previousBlock, block.getPreviousBlock());
    }

}



