<div align="left" style="position: relative;">
<img src="https://img.icons8.com/?size=512&id=55494&format=png" align="right" width="30%" style="margin: -20px 0 0 20px;">
<h1>OMNIVIEW_SNMP</h1>
<p align="left">
</p>
<p align="left">
	<img src="https://img.shields.io/github/last-commit/theiyappan/OmniView_SNMP?style=for-the-badge&logo=git&logoColor=white&color=2d2098" alt="last-commit">
	<img src="https://img.shields.io/github/languages/top/theiyappan/OmniView_SNMP?style=for-the-badge&color=2d2098" alt="repo-top-language">
	<img src="https://img.shields.io/github/languages/count/theiyappan/OmniView_SNMP?style=for-the-badge&color=2d2098" alt="repo-language-count">
</p>
<p align="left">Built with the tools and technologies:</p>
<p align="left">
	<img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="java">
</p>
</div>
<br clear="right">

##  Table of Contents

- [ Overview](#-overview)
- [ Features](#-features)
- [ Project Structure](#-project-structure)
  - [ Project Index](#-project-index)
- [ Getting Started](#-getting-started)
  - [ Prerequisites](#-prerequisites)
  - [ Installation](#-installation)
  - [ Usage](#-usage)
  - [ Testing](#-testing)
- [ Project Roadmap](#-project-roadmap)
- [ Contributing](#-contributing)
- [ License](#-license)
- [ Acknowledgments](#-acknowledgments)

---

##  Overview

**OmniView_SNMP** is a robust, portable Java application designed for network administrators and developers to interact with SNMP agents. Unlike traditional heavy Network Management Systems (NMS), OmniView is **MIB-less**, meaning it relies on an internal dictionary and smart formatting rather than requiring external `.mib` files to be loaded.

It is specifically engineered to handle **SNMPv3** complexity, featuring a custom **Manual Engine ID Injection** mechanism that solves common "Unknown User Name" and discovery timeout errors often encountered when connecting to Net-SNMP agents.

---

##  Features

* **Protocol Support:** Full support for SNMP **v1**, **v2c**, and **v3**.
* **Advanced Security:** Implements SNMPv3 USM with support for:
    * `noAuthNoPriv` (Level 1)
    * `authNoPriv` (Level 2 - MD5)
    * `authPriv` (Level 3 - MD5/SHA + DES)
* **MIB-less Browsing:** Instantly view OID trees without configuring external MIB files, using a built-in `OidDictionary`.
* **Manual Engine ID:** Bypasses unstable discovery phases by allowing hardcoded authoritative Engine IDs.
* **Smart Formatting:** Automatically detects and converts `OctetStrings` (Hex vs. ASCII) into human-readable text.
* **Operations:** Supports `WALK`, `GET`, `GET_NEXT`, `GET_BULK`, and `SET`.

---

##  Project Structure

```sh
└── OmniView_SNMP/
    ├── README.md
    ├── bin
    │   └── com
    ├── lib
    │   └── snmp4j-3.8.2.jar
    ├── manifest.txt
    ├── pom.xml
    └── src
        └── main
```


###  Project Index
<details open>
	<summary><b><code>OMNIVIEW_SNMP/</code></b></summary>
	<details> <!-- __root__ Submodule -->
		<summary><b>__root__</b></summary>
		<blockquote>
			<table>
			<tr>
				<td><b><a href='https://github.com/theiyappan/OmniView_SNMP/blob/master/manifest.txt'>manifest.txt</a></b></td>
				<td><code>❯ REPLACE-ME</code></td>
			</tr>
			</table>
		</blockquote>
	</details>
	<details> <!-- src Submodule -->
		<summary><b>src</b></summary>
		<blockquote>
			<details>
				<summary><b>main</b></summary>
				<blockquote>
					<details>
						<summary><b>java</b></summary>
						<blockquote>
							<details>
								<summary><b>com</b></summary>
								<blockquote>
									<details>
										<summary><b>omniview</b></summary>
										<blockquote>
											<table>
											<tr>
												<td><b><a href='https://github.com/theiyappan/OmniView_SNMP/blob/master/src/main/java/com/omniview/Main.java'>Main.java</a></b></td>
												<td><code>❯ REPLACE-ME</code></td>
											</tr>
											</table>
											<details>
												<summary><b>model</b></summary>
												<blockquote>
													<table>
													<tr>
														<td><b><a href='https://github.com/theiyappan/OmniView_SNMP/blob/master/src/main/java/com/omniview/model/SnmpResult.java'>SnmpResult.java</a></b></td>
														<td><code>❯ REPLACE-ME</code></td>
													</tr>
													</table>
												</blockquote>
											</details>
											<details>
												<summary><b>utils</b></summary>
												<blockquote>
													<table>
													<tr>
														<td><b><a href='https://github.com/theiyappan/OmniView_SNMP/blob/master/src/main/java/com/omniview/utils/OidDictionary.java'>OidDictionary.java</a></b></td>
														<td><code>❯ REPLACE-ME</code></td>
													</tr>
													<tr>
														<td><b><a href='https://github.com/theiyappan/OmniView_SNMP/blob/master/src/main/java/com/omniview/utils/SmartFormatter.java'>SmartFormatter.java</a></b></td>
														<td><code>❯ REPLACE-ME</code></td>
													</tr>
													</table>
												</blockquote>
											</details>
											<details>
												<summary><b>service</b></summary>
												<blockquote>
													<table>
													<tr>
														<td><b><a href='https://github.com/theiyappan/OmniView_SNMP/blob/master/src/main/java/com/omniview/service/ScanWorker.java'>ScanWorker.java</a></b></td>
														<td><code>❯ REPLACE-ME</code></td>
													</tr>
													</table>
												</blockquote>
											</details>
											<details>
												<summary><b>ui</b></summary>
												<blockquote>
													<table>
													<tr>
														<td><b><a href='https://github.com/theiyappan/OmniView_SNMP/blob/master/src/main/java/com/omniview/ui/MainFrame.java'>MainFrame.java</a></b></td>
														<td><code>❯ REPLACE-ME</code></td>
													</tr>
													<tr>
														<td><b><a href='https://github.com/theiyappan/OmniView_SNMP/blob/master/src/main/java/com/omniview/ui/TreeHelper.java'>TreeHelper.java</a></b></td>
														<td><code>❯ REPLACE-ME</code></td>
													</tr>
													<tr>
														<td><b><a href='https://github.com/theiyappan/OmniView_SNMP/blob/master/src/main/java/com/omniview/ui/SnmpTreeCellRenderer.java'>SnmpTreeCellRenderer.java</a></b></td>
														<td><code>❯ REPLACE-ME</code></td>
													</tr>
													</table>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
</details>

---
##  Getting Started

###  Prerequisites

Before getting started with OmniView_SNMP, ensure your runtime environment meets the following requirements:

- **Programming Language:** Java


###  Installation

Install OmniView_SNMP using one of the following methods:

**Build from source:**

1. Clone the OmniView_SNMP repository:
```sh
❯ git clone https://github.com/theiyappan/OmniView_SNMP
```

2. Navigate to the project directory:
```sh
❯ cd OmniView_SNMP
```

3. Install Net-Snmp for detailed testing(using choco):

Windows:
```sh
❯ choco install net-snmp
```
For manual installing for windows, visit:https://sourceforge.net/projects/net-snmp/files/net-snmp%20binaries/

Linux:
```sh
❯ sudo apt-get update
❯ sudo apt-get install snmp snmpd libsnmp-dev
```

Mac:
```sh
❯ brew install net-snmp
```


###  Usage
Run OmniView_SNMP by running the .bat file


##  Contributing

- **💬 [Join the Discussions](https://github.com/theiyappan/OmniView_SNMP/discussions)**: Share your insights, provide feedback, or ask questions.
- **🐛 [Report Issues](https://github.com/theiyappan/OmniView_SNMP/issues)**: Submit bugs found or log feature requests for the `OmniView_SNMP` project.
- **💡 [Submit Pull Requests](https://github.com/theiyappan/OmniView_SNMP/blob/main/CONTRIBUTING.md)**: Review open PRs, and submit your own PRs.

<details closed>
<summary>Contributing Guidelines</summary>

1. **Fork the Repository**: Start by forking the project repository to your github account.
2. **Clone Locally**: Clone the forked repository to your local machine using a git client.
   ```sh
   git clone https://github.com/theiyappan/OmniView_SNMP
   ```
3. **Create a New Branch**: Always work on a new branch, giving it a descriptive name.
   ```sh
   git checkout -b new-feature-x
   ```
4. **Make Your Changes**: Develop and test your changes locally.
5. **Commit Your Changes**: Commit with a clear message describing your updates.
   ```sh
   git commit -m 'Implemented new feature x.'
   ```
6. **Push to github**: Push the changes to your forked repository.
   ```sh
   git push origin new-feature-x
   ```
7. **Submit a Pull Request**: Create a PR against the original project repository. Clearly describe the changes and their motivations.
8. **Review**: Once your PR is reviewed and approved, it will be merged into the main branch. Congratulations on your contribution!
</details>



