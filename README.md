# A graphical user interface that is easily portable.

The focus is on portability, so that it runs everywhere.
To port this project to an other system you have only to implement [IFrame](core/src/com/starcom/ui/frame/IFrame.java)

There are already some ports:
- Swing [SwingFrame](impl/swing/src/com/starcom/ui/frame/impl/SwingFrame.java)
- others... (planned is LWJGL and ANDROID)

Using in other projects:
- First add jitpack repo: [Jitpack](https://jitpack.io/)
- Then add port-ui-core and an implementation.
```
implementation 'com.github.Starcommander:port-ui:core:1.0'
implementation 'com.github.Starcommander:port-ui:portui-impl-swing:1.0'
```
- See also [Jitpack](https://jitpack.io/)
