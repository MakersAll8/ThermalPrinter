This is a demo to print receipts on USB zj58 thermal printers from Ubuntu

This demo is not an official demo, it is a simple hack based on my limited
understanding of the official android demo.

It is expected that you run into Permission Denied Exception if your user does
not have permission to access the printer resource on your OS. Open up your
command line and run
  - sudo usermod -a -G lp <your_ubuntu_username>
Log out and log in, or reboot.

Also, your Thermal printer might not be mounted at /dev/usb/lp2 like mine. Run
  - ll /dev/usb/ to see how many printers you have.
  - lpstat -s could be useful too

Feel free to use, or modify this repo in any way you want. I am not liable to
any harm or damages caused in anyway.

这个Demo展示了如何从Ubuntu系统通过资江58热敏打印机打印小票。

这个Demo不是官方的Demo，而是基于我对官方安卓Demo有限的理解做的一个简单粗暴版本。

你直接跑这个程序的话会遇到Permission Denied Exception权限不够。打开命令行跑
  - sudo usermod -a -G lp <你的用户名>
退出、登录、或者重启。

此外，你的热敏打印机不一定挂载在 /dev/usb/lp2 这个路径。跑
  - ll /dev/usb/ 来查看你有几台打印机
  - lpstat -s 也可能有帮助

这些代码你可以随意使用、修改。我对任何因此带来的任何伤害或者损坏不承担责任。