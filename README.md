# Hoshi-Note

## Introduction

Hoshi-Note 是一个云笔记系统，由 Hoshi-UI 模块提供前端界面。

基于 Spring Boot 与 Spring Cloud 开发，采用微服务及分布式模式部署，集群使用 Kubernetes 进行管理。

## Features

- 容器化部署，同时采用 Kubernetes 对容器进行编排
- Spring Boot 与 Spring Cloud 开发，并集成 Spring Cloud Kubernetes，为微服务提供原生支持的服务发现与配置管理
- 单点登录支持
- React 编写前端组件，实现前后的分离
- 使用 MinIO、CockroachDB、Redis、RabbitMQ 服务组件，原生支持分布式部署
- Grafana、Prometheus、Loki 作为集群指标与日志收集的监控平台
- 使用 GitHub Actions 与 GitHub Packages 实现持续集成与持续部署

## Installation

```shell
mvn package -DskipTests
java -jar /app/<module>-1.0-SNAPSHOT.jar
```

## Function and Architecture

![function](./docs/function.png)

![architecture](./docs/architecture.png)

## Render

![render1](./docs/render1.png)

![render2](./docs/render2.png)

![render3](./docs/render3.png)

![render4](./docs/render4.png)

![render5](./docs/render5.png)

![render6](./docs/render6.png)

![render7](./docs/render7.png)

![render8](./docs/render8.png)

![render9](./docs/render9.png)

![render10](./docs/render10.png)

![render11](./docs/render11.png)

![render12](./docs/render12.png)

![render13](./docs/render13.png)

![render14](./docs/render14.png)

![render15](./docs/render15.png)

![render16](./docs/render16.png)

![render17](./docs/render17.png)

![render18](./docs/render18.png)

![render19](./docs/render19.png)

![render20](./docs/render20.png)

![render21](./docs/render21.png)

![render22](./docs/render22.png)

![render23](./docs/render23.png)

![render24](./docs/render24.png)

![render25](./docs/render25.png)

![render26](./docs/render26.png)

![render27](./docs/render27.png)

![render28](./docs/render28.png)

![render28](./docs/render28.png)

![render30](./docs/render30.png)

![render31](./docs/render31.png)

![render32](./docs/render32.png)

![render33](./docs/render33.png)

![render34](./docs/render34.png)

![render35](./docs/render35.png)

![render36](./docs/render36.png)

## Maintainer

hoshi-note 由 [Otstar Lin](https://ixk.me/)
和下列 [贡献者](https://github.com/syfxlin/hoshi-note/graphs/contributors)
的帮助下撰写和维护。

> Otstar Lin - [Personal Website](https://ixk.me/) · [Blog](https://blog.ixk.me/) · [Github](https://github.com/syfxlin)

## License

![License](https://img.shields.io/github/license/syfxlin/hoshi-note.svg?style=flat-square)

根据 Apache License 2.0 许可证开源。
