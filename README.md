# cryptoeditor
This is an editor which encrypts your files or passwords you want to protect. You can encrypt files and directories or create your own inside the editor. I made use of assymetric encryption to protect the data, a 2048 bit RSA key pair is used in combination with a 256 bit AES key. In order to make this vault secure offline, the RSA private key must be stored inside an external storage medium (e.g. USB-Stick, CD, SD-Card) and it only needs to be inserted to open the vault. It should then be unplugged immediately. The encrypted data and the keys are stored with jdom2 in .xml files. JavaFX is used for the GUI and Maven as the build-tool.

## IMPORTANT:
Important data like credit card numbers or similar should not be stored in this vault! I'm not a professional cryptographer.
