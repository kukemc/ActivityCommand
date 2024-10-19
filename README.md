

# ActivityCommand
A Minecraft Plugin

### 中文

**简介**
这是一个简单、轻量且高效的活动指令插件。
它允许服务器管理员方便地创建服务器内的活动事件。
当玩家在指定的活动日期内第一次登录服务器时，会触发相应的指令。

**配置示例**

`config.yml` 文件示例：

```yaml
events:
  "2024-09-17":
    commands:
      - "give %player% diamond 1"
      - "say 欢迎 %player%，你获得了登录奖励！"
  "2024-12-25":
    commands:
      - "give %player% emerald 5"
      - "say %player% 圣诞快乐，%player%！"
```

---

### English

**Description**
This is a simple, lightweight, and efficient event command plugin. It allows server administrators to conveniently create in-game events. When players log into the server for the first time within the specified event dates, the corresponding commands will be triggered.

**Configuration Example**

Example of `config.yml`:

```yaml
events:
  "2024-09-17":
    commands:
      - "give %player% diamond 1"
      - "say Welcome %player%, you've received your login reward!"
  "2024-12-25":
    commands:
      - "give %player% emerald 5"
      - "say Merry Christmas, %player%!"
```
