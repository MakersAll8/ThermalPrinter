This is a demo to print receipts on USB zj58 thermal printers from Ubuntu
This demo is not an official demo, it is a simple hack based on my limited
understanding of the official android demo.
It is expected that you run into Permission Denied exception if your user does
not have permission to access the printer resource on your OS. Open up your
command line and run
  - sudo usermod -a -G lp <ubuntu_user>
Log out and log in, or reboot.

Also, your Thermal printer might not be mounted at /dev/usb/lp2 like mine. Run
  - ll /dev/usb/ to see how many printers you have.
  - lpstat -s could be useful too

N.B. There are hex code that allow you to align center, align right, etc. I
encourage you to explore the official demo for Android. It is trivial to
adapt code you need to achieve alignment.

Feel free to use, or modify this repo in any way you want. I am not liable to
any harm or damages caused in anyway.
