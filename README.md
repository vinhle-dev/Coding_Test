
# Wordle Game

A brief description of what this project does and who it's for

## 🛠 System Requirements
* Java 21
* Maven 3.x

## 🚀 Installation and Running

* Clone the project

```bash
  git clone https://github.com/vinhle-dev/Coding_Test.git
  cd Coding_Test
```

* Build and create the JAR

```bash
  mvn clean package
```

The JAR file will be located in the target/ directory:

```bash
  target/Coding_Test-Demo.jar
```

* Run the application

```bash
  java -jar target/Coding_Test-Demo.jar
```
## 🧠 Algorithm Explanation

The algorithm consists of two main steps:

1. First, based on the length of the word, the algorithm will try to sequentially take characters from a-z. For example, if the length is 3, the words used for guessing will be abc, def, etc. The goal is to scan the entire alphabet and find the characters present in the target word as quickly as possible.

2. From the list of characters found in step 1, the algorithm will take these characters and replace the positions where the correct character has not been found to determine the exact position of the character.

3. After completing step 2, the algorithm will have identified the target word.

## 📝 Notes

While implementing the program, I used ChatGPT to help me write this Readme document. I also used ChatGPT to find better optimization methods for the program after it was completed.

## Contributing

Contributions are always welcome!

If you would like to contribute to the project, please fork this repository and create a pull request.


## 📄 License

This project is released under the [MIT](https://choosealicense.com/licenses/mit/) License.


