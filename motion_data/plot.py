import matplotlib.pyplot as plt

f = open("data.txt", "rb")

data = map(float, f.read().replace("\n", "")[1:].split(","))
sample_length = 50

while True:
    threshold = float(raw_input("Threshold: "))
    num_partitions = len(data)/sample_length

    moving_data = []

    for i in range(num_partitions):
        partition = data[i*sample_length:(i+1)*sample_length]
        rang = max(partition) - min(partition)
        for j in range(sample_length): moving_data.append((rang > threshold))

    last_partition = data[num_partitions*sample_length:len(data)]
    rang = max(last_partition) - min(last_partition)
    for j in range(len(last_partition)): moving_data.append((rang > threshold))

    plt.plot(data)
    plt.plot(moving_data, 'r', linewidth=3)
    plt.show()
