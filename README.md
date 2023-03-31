## Simple Blockchain Example in Java

This is a simple blockchain project that was created with the aim of helping understand the basic concepts of blockchain. The project is written in Java and uses basic technologies to create a basic blockchain.

### Inspiration

This project was inspired by the article "Blockchain Development — Zero to Hero [Guide]", which can be found on Medium at the following link: https://medium.com/programmers-blockchain/blockchain-development-mega-guide-5a316e6d10df

The article provides a comprehensive overview of blockchain technology, including the basic concepts, the underlying algorithms, and the practical applications of blockchain technology. It also includes a step-by-step guide to building a blockchain from scratch using the Java programming language.

We used the article as a reference and a source of inspiration for building our blockchain project using Java. By following the basic principles outlined in the article, we were able to create a simple yet functional blockchain system that demonstrates the essential features of blockchain technology.

We highly recommend the "Blockchain Development — Zero to Hero [Guide]" article to anyone who is interested in learning more about blockchain technology and how it works. It provides a clear and concise explanation of the key concepts and algorithms that underpin blockchain technology, as well as practical examples and step-by-step instructions for building your own blockchain system.

#### Features

The simple blockchain project is capable of performing the following features:

- Add blocks to the chain

- Validate the integrity of the chain
- Validate the integrity of a specific block

#### Simplified Block Structure

In this project, we simplify the structure of a blockchain block by only allowing one transaction per block. This simplification allows us to focus on the core concepts of blockchain technology, such as the proof of work algorithm and the verification of blocks.

In a real-world blockchain system, a block may contain multiple transactions, which can increase the complexity of the system. However, the basic principles of blockchain technology remain the same regardless of the number of transactions per block.

By only allowing one transaction per block in this project, we can keep the code simple and easy to understand. This also allows us to focus on the essential features of a blockchain, such as the creation of new blocks, the verification of transactions, and the proof of work algorithm.

#### Proof of Work

Proof of Work is a consensus algorithm that is used in blockchain networks to ensure that new blocks are added to the blockchain in a secure and trustless way. The basic idea behind Proof of Work is to require that a certain amount of computational work is done in order to add a new block to the blockchain.

In this project, we use a Proof of Work algorithm to ensure that new blocks are added to the blockchain with a certain level of difficulty. The difficulty level is defined as the number of zeros that must be found at the beginning of the hash of the block.

To add a new block to the blockchain, a miner must first generate a random string called a nonce. The miner then combines the data of the block with the nonce and computes the hash of the resulting string. If the hash has the required number of zeros at the beginning, the block is considered valid and can be added to the blockchain.

The difficulty of the Proof of Work algorithm is adjusted periodically to ensure that new blocks are added to the blockchain at a consistent rate. If the difficulty is too low, new blocks will be added too quickly, which can lead to a security vulnerability called a 51% attack. If the difficulty is too high, new blocks will be added too slowly, which can lead to a slow and inefficient blockchain network.

By using a Proof of Work algorithm, we can ensure that new blocks are added to the blockchain in a secure and trustless way, without the need for a centralized authority to validate transactions.

#### Techniques used

##### Behavior Driven Development (BDD)

Behavior Driven Development (BDD) is an agile software development methodology that emphasizes collaboration between developers, testers, and stakeholders to ensure that the software meets the business requirements. BDD focuses on the behavior of the system and encourages the use of natural language to describe the expected behavior of the system in different scenarios.

BDD uses a common language called Gherkin to describe the behavior of the system in terms of features, scenarios, and steps. Gherkin is a human-readable language that is easy to understand by non-technical stakeholders. Gherkin scenarios are written in a format that is similar to natural language and can be easily translated into automated tests.

BDD helps us to ensure that the blockchain system meets the business requirements, and that the features of the system are well defined and documented. It also helps us to collaborate with stakeholders to ensure that everyone has a common understanding of the system's behavior.

##### Builder Pattern

The Builder pattern is a design pattern that is used to create complex objects by separating the construction of the object from its representation. The Builder pattern separates the construction of the object into different steps, which allows the construction process to be more flexible and easier to maintain.

##### SOLID Principles

The SOLID principles are a set of five principles that are used to design object-oriented software. The SOLID principles stand for:

- Single Responsibility Principle (SRP)
- Open/Closed Principle (OCP)
- Liskov Substitution Principle (LSP)
- Interface Segregation Principle (ISP)
- Dependency Inversion Principle (DIP)

#### How to Use

##### Prerequisites

Java 17 installed on your machine
Maven installed on your machine

##### Running the Project

Clone this repository on your machine using the following command:

```bash
git clone https://github.com/your-username/simple-blockchain-java.git
```

Access the project directory:

```bash
mvn clean package
```

Run the project using the following command:

```bash
mvn compile exec:java -Dexec.mainClass="br.com.lucianoyamane.example.dundermifflin.ExecuteBlockChain" -Dexec.cleanupDaemonThreads=false
```

### Conclusion

This is a simple blockchain project written in Java that can help understand the basic concepts of blockchain. If you have any suggestions or ideas on how to improve this project, feel free to contribute with a pull request.