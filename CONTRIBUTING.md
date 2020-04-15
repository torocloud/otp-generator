# Contributor's Guide

Issues can be submitted by anyone - seasoned developers or newbies.

### Getting Started

1.  Fork the project on [GitHub](https://github.com/torocloud/otp-generator).

2.  Clone the project.

3.  Create a branch specific to the issue you are working on.

    ```shell
    git checkout -b update-readme-file
    ```

    For clarity, name your branch `update-xxx` or `fix-xxx`. The `xxx` is a short description of the changes you're making. Examples include `update-readme` or `fix-typo-on-contribution-md`.

5.  Open up the project in your favorite text editor, select the file you want to contribute to, and make your changes.

6.  Add your modified files to git.

    ```shell
    git add path/to/filename.ext
    ```

    You can also add all unstaged files using:

    ```shell
    git add .
    ```

    **Note:** using a `git add .` will automatically add all files. You can do a
    `git status` to see your changes, but do it **before** `git add`.

6.  Commit your changes using a descriptive commit message.

    ```shell
    git commit -m "Brief Description of Commit"
    ```

7.  Push your commits to your GitHub Fork:

    ```shell
    git push -u origin branch-name
    ```

8.  Submit a pull request.

    Within GitHub, visit this main repository and you should see a banner
    suggesting to make a pull request. While you're writing up the pull
    request, you can add `Closes #XXX` in the message body where `#XXX` is the
    issue you're fixing. So an example would be `Closes #42` would close issue
    `#42`.

### Submitting a Pull Request

If you decide to fix an issue, it's advisable to check the comment thread to see if there's somebody already working on a fix. If no one is working on it, kindly leave a comment stating that you intend to work on it. That way other people don't accidentally duplicate your effort.

In a situation whereby somebody decides to fix an issue but doesn't follow up for a particular period of time, say 2-3 weeks, it's acceptable to still pick up the issue but make sure to leave a comment.