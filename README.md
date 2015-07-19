# Block-Cipher-SPECK
This program is an implementtion of Block Cipher SPECK

It consist of 4 files

Encrypt.java

Decrypt.java

EncryptFile.java

DecryptFile.java

Block Cipher SPECK

SPECK is a family of block cipher algorithms published by the U.S. National Security Agency in this 2013 paper:

R. Beaulieu, D. Shors, J. Smith, S. Streatman-Clark, B. Weeks, and L. Wingers. The SIMON and SPECK families of lightweight block ciphers. Cryptology ePrint Archive, Report 2013/404, June 19, 2013.  http://eprint.iacr.org/2013/404

This cipher implementation encrypts block of 32 bit with key of 64 bit and has 32 rounds of encryption

For this implementation i have used parallel java library for cryptography

To run this program follow the instructions

Download and include parallel java library to your project, link to parallel java library: http://www.cs.rit.edu/~ark/pj2.shtml

execute the following commands

ENCRYPTION:

Encrypt single message:

java Encrypt <Key> <PlainText>

<key> is the key (16 hexadecimal digits, no spaces).

<plaintext> is the plaintext block (8 hexadecimal digits, no spaces).

The output of this program will be Ciphertext of 8 Hexadecimal digits.If the input requirements are not meet, then error message will be displayed

Encrypt file:

java EncryptFile <key> <ptfile> <ctfile>

<key> is the key (16 hexadecimal digits, no spaces).

<ptfile> is the name of the input file containing the plaintext message bytes.

<ctfile> is the name of the output file containing the ciphertext message bytes.

The output file must contain only the ciphertext message bytes produced by encrypting the plaintext message bytes in the given input file, using the SPECK32/64 block cipher algorithm with the given key. The program does not print anything on std output.


DECRYPTION

Decryt single message:

java Decrypt <key> <ciphertext>

<key> is the key (16 hexadecimal digits, no spaces).

<ciphertext> is the ciphertext block (8 hexadecimal digits, no spaces).

The output of this program will be Plaintext message of 8 hex digits.

Decrypt file:

java DecryptFile <key> <ctfile> <ptfile>

<key> is the key (16 hexadecimal digits, no spaces).

<ctfile> is the name of the input file containing the ciphertext message bytes.

<ptfile> is the name of the output file containing the plaintext message bytes.

THe output file must contain only the plaintext message bytes produced by decrypting the ciphertext message bytes in the given input file, using the SPECK32/64 block cipher algorithm with the given key. The program does not print anything on std output


