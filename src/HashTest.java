public class HashTest {
    public static void main(String[] args) {
        HashTable<Integer> nums = new HashTable<Integer>();
        for (int i = 0; i < 20; i++) {
            int x = (int)(Math.random()*10000);
            nums.add(x);
        }
        System.out.println(nums);

    }
}
