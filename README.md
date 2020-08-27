# javaxf-offline-vault
This is a vault for storing passwords and files offline. I made use of assymetric encryption to protect your data, a 2048 bit RSA key pair is used in combination with a 256 bit AES key. In order to make this vault secure offline, the RSA private key must be stored inside an external storage medium (e.g. USB-Stick, CD, SD-Card) and only needs to be inserted to open the vault. It should then be unplugged immediately. The encrypted data and the keys are stored with jdom2 in .xml files. JavaFX is used for the GUI and Maven as the build-tool.

## IMPORTANT:
Important data like credit card numbers or similar should not be stored in this vault. I'm not a professional cryptographer and I started this project to understand basic cryptography and JavaFX. In addition I wanted to have a small vault to organize all of my passwords and some other data. 
