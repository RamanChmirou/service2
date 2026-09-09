package com.mpie.service2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class KeySecretStoreTest {

    private static final String DEFAULT_SECRET = "Top‑secret message";

    private KeySecretStore store;

    @BeforeEach
    void setUp() {
        store = new KeySecretStore();
    }

    @Nested
    @DisplayName("Storage")
    class StorageTests {

        @Test
        @DisplayName("should return the same String key after successful storage")
        void shouldReturnSameStringKey() {
            // given
            String key = "my-Custom-Key";

            // when
            String returnedKey = store.storage(key, DEFAULT_SECRET);

            // then
            assertEquals(key, returnedKey, "Store should echo the provided key");
        }

        @Test
        @DisplayName("should return the same Integer key after successful storage")
        void shouldReturnSameIntegerKey() {
            // given
            Integer key = 123;

            // when
            Integer returnedKey = store.storage(key, DEFAULT_SECRET);

            // then
            assertEquals(key, returnedKey, "Store should echo the provided key");
        }

        @ParameterizedTest(name = "rejects key longer than 20 characters: '{0}'")
        @ValueSource(strings = {"abcdefghijklmnopqrstu", "too-long-custom-key-123"})
        @DisplayName("should reject keys longer than 20 characters")
        void shouldRejectKeyLongerThanTwentyCharacters(String tooLongKey) {
            // expect
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> store.storage(tooLongKey, DEFAULT_SECRET));
            assertTrue(ex.getMessage().contains("20"),
                    "Message should describe length constraint");
        }

        @ParameterizedTest(name = "rejects blank or null key='{0}', secret='{1}'")
        @CsvSource(value = {
                "''          , secret" ,
                "null        , secret" ,
                "key         , ''"     ,
                "key         , null"   }, nullValues = "null")
        @DisplayName("should reject blank or null key / secret")
        void shouldRejectBlankOrNullKeyOrSecret(String key, String secret) {
            // expect
            assertThrows(IllegalArgumentException.class, () -> store.storage(key, secret));
        }

        @Test
        @DisplayName("should reject duplicate key")
        void shouldRejectDuplicateKey() {
            // given
            String duplicateKey = "DUPLICATE";
            store.storage(duplicateKey, DEFAULT_SECRET);

            // expect
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> store.storage(duplicateKey, "second"));
            assertTrue(ex.getMessage().contains(duplicateKey.toLowerCase()),
                    "Message should mention duplicate key");
        }
    }

    @Nested
    @DisplayName("Retrieval")
    class RetrievalTests {

        @ParameterizedTest(name = "stored='{0}', retrieved='{1}' (case‑insensitive)")
        @CsvSource({
                "MyKey, mykey",
                "MyKey, MYKEY",
                "my-key, MY-KEY",
                "mY-cUsTom-kEy, MY-CUSTOM-KEY"})
        @DisplayName("should retrieve secret regardless of key case")
        void shouldRetrieveSecretCaseInsensitive(String storedKey, String requestedKey) {
            // given
            store.storage(storedKey, DEFAULT_SECRET);

            // when
            String result = store.retrieve(requestedKey).get();

            // then
            assertEquals(DEFAULT_SECRET, result, "Secret should be retrievable irrespective of case");
        }

        @Test
        @DisplayName("should retrieve secret stored with Integer key")
        void shouldRetrieveSecretUsingIntegerKey() {
            // given
            Integer key = 10101;
            store.storage(key, DEFAULT_SECRET);

            // when
            String result = store.retrieve(key).get();

            // then
            assertEquals(DEFAULT_SECRET, result);
        }

        @Test
        @DisplayName("should throw when key is not found")
        void shouldNotThrowWhenKeyNotFound() {
            // expect
            assertDoesNotThrow(() -> store.retrieve("unknown"));
        }
    }
}
