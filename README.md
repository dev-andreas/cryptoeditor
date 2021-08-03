# cryptoeditor
An editor which can encryptfiles and passwords. You can encrypt data or create your own inside the editor.
- Assymetric encryption, a 2048 bit RSA key pair is used in combination with a 256 bit AES key. In order to make this vault secure offline, the RSA private key must be stored inside an external storage medium (e.g. USB-Stick, CD, SD-Card) and should be only inserted into your PC if you want to open the vault. 

## To sum it all up:
- JavaFX is used for the GUI.
- 2048 bit RSA + 256 bit AES encryption.
- USB Stick or similar can be used as a token.

## IMPORTANT:
### Vulnerable data e.g. credit card numbers or similar should not be stored in this vault! I'm not a professional cryptographer.

## TODO:
- Update theme
- Automatically create new data files when deleted
