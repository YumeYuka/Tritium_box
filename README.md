## Tritium box

### 简介

`Tritium_box 是一个基于 CuprumTurbo-Scheduler 来调度性能,对模块进行封装的产物`

## 原因

> 起初我们认为大部分人都能够正常刷写并使用模块，但为了解决一些小白的问题，我们制作了[文档站点](https://tritium.nightrainmilkyway.cn/)，里面详细介绍了如何使用，如何刷写，如何安装等，但仍然纯在一些用户`不阅读文档`虽然我们已经详细介绍了如何使用，于是我们决定对模块进行封装，现在只需要点击安装即可，开箱即用。

## 一些额外的原因

  已经有了`cutoolbox`，为什么还要制作[tritium_box](https://tritium.nightrainmilkyway.cn/),当然我们完全可以反编译`cutoolbox`，将我们的配置文件内置，但我们并不希望这样做，因为`cutoolbox`的作者[chenzyadb](https://github.com/chenzyadb)已经为我们留了导入配置功能的使用，但有些用户在看完我们的文档之后仍然无法正确导入使用,于是我们决定制作`tritium_box`.

 我们明确表明非常建议用户使用Cutoolbox，而Tritium_box只是作为最差一种选择
## 附加

这是我写的第一个`APP`,所以可能存在一些问题，如果发现请及时联系我，我会尽快修复
##
[Apache-2.0 license](https://github.com/TimeBreeze/Tritium_box/blob/master/LICENSE)

## 鸣谢

[CuprumTurbo Scheduler](https://github.com/chenzyadb/CuprumTurbo-Scheduler) : 一个简单可靠的性能调度.

[CuJankDetector](https://github.com/chenzyadb/CuJankDetector): 钩住 surfaceflinger 以检测卡顿。