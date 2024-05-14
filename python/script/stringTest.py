import sys

def main():
    print("Hello from Python script!")
    if len(sys.argv) > 1:
        print(f"Received argument: {sys.argv[1]}")

if __name__ == "__main__":
    main()