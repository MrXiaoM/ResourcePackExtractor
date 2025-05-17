# ResourcePackExtractor

Minecraft 混淆资源包导出器。

## 简介

这个工具用于导出所谓“无法解压”的资源包中的文件，从 [FabricMC/yarn](https://github.com/FabricMC/yarn) 1.20.4 的客户端反混淆代码中复制资源包读取逻辑（其实就是 Java 自带的 ZipFile，原汁原味），进行解压操作。

## 用法

需要使用 Java 17 或以上运行
```
java -jar ResourcePackExtractor-1.0.0-all.jar -i path/to/ResourcePack.zip
```
