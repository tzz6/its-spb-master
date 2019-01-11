function Encrypt(word){
		 var key = CryptoJS.enc.Utf8.parse('0102030405060708');
		 var iv  = CryptoJS.enc.Utf8.parse('0102030405060708');	
		 var srcs = CryptoJS.enc.Utf8.parse(word);
		 var encrypted = CryptoJS.AES.encrypt(srcs, key, { iv: iv,mode:CryptoJS.mode.CBC});
         return encrypted.toString();
}